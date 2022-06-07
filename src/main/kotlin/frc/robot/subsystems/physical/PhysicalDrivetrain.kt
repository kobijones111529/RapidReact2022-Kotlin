package frc.robot.subsystems.physical

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.InvertType
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.DoubleSolenoid.Value
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.PneumaticsModuleType
import frc.robot.subsystems.Drivetrain
import frc.robot.utils.SimpleMotorFeedforwardConstants
import frc.robot.utils.VariableSlewRateLimiter
import si.uom.SI
import si.uom.quantity.AngularAcceleration
import si.uom.quantity.AngularSpeed
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Acceleration
import javax.measure.quantity.Length
import javax.measure.quantity.Speed

class PhysicalDrivetrain(private val properties: Properties) : Drivetrain {
  data class Properties(
    val driveFrontLeftID: Int,
    val driveFrontRightID: Int,
    val driveBackLeftID: Int,
    val driveBackRightID: Int,
    val driveLeftEncoderChannelA: Int,
    val driveLeftEncoderChannelB: Int,
    val driveRightEncoderChannelA: Int,
    val driveRightEncoderChannelB: Int,
    val pneumaticsModuleType: PneumaticsModuleType,
    val shiftForwardChannel: Int,
    val shiftReverseChannel: Int,
    val shiftLowGearValue: Value,
    val driveLeftFeedforwardData: SimpleMotorFeedforwardConstants,
    val driveRightFeedforwardData: SimpleMotorFeedforwardConstants,
    val driveEncoderDistancePerPulse: Quantity<Length>,
    val trackWidth: Quantity<Length>
  ) {
    val shiftHighGearValue: Value
      get() {
        return when (shiftLowGearValue) {
          Value.kForward -> Value.kReverse
          Value.kReverse -> Value.kForward
          Value.kOff -> Value.kOff
        }
      }
  }

  private val driveFrontLeft = WPI_TalonSRX(properties.driveFrontLeftID)
  private val driveFrontRight = WPI_TalonSRX(properties.driveFrontRightID)
  private val driveBackLeft = WPI_TalonSRX(properties.driveBackLeftID)
  private val driveBackRight = WPI_TalonSRX(properties.driveBackRightID)

  private val driveLeftEncoder = Encoder(properties.driveLeftEncoderChannelA, properties.driveLeftEncoderChannelB)
  private val driveRightEncoder = Encoder(properties.driveRightEncoderChannelA, properties.driveRightEncoderChannelB)

  private val shifter =
    DoubleSolenoid(properties.pneumaticsModuleType, properties.shiftForwardChannel, properties.shiftReverseChannel)

  private val kinematics: DifferentialDriveKinematics =
    DifferentialDriveKinematics(properties.trackWidth.to(SI.METRE).value.toDouble())

  private val driveLeftFeedforward =
    SimpleMotorFeedforward(
      properties.driveLeftFeedforwardData.s,
      properties.driveLeftFeedforwardData.v,
      properties.driveLeftFeedforwardData.a
    )
  private val driveRightFeedforward =
    SimpleMotorFeedforward(
      properties.driveRightFeedforwardData.s,
      properties.driveRightFeedforwardData.v,
      properties.driveRightFeedforwardData.a
    )

  init {
    driveFrontLeft.configFactoryDefault()
    driveFrontRight.configFactoryDefault()
    driveBackLeft.configFactoryDefault()
    driveBackRight.configFactoryDefault()

    driveFrontLeft.enableVoltageCompensation(true)
    driveFrontRight.enableVoltageCompensation(true)
    driveBackLeft.enableVoltageCompensation(true)
    driveBackRight.enableVoltageCompensation(true)

    driveBackLeft.follow(driveFrontLeft)
    driveBackRight.follow(driveFrontRight)

    driveFrontLeft.setInverted(InvertType.None)
    driveFrontRight.setInverted(InvertType.None)
    driveBackLeft.setInverted(InvertType.FollowMaster)
    driveBackRight.setInverted(InvertType.FollowMaster)

    driveLeftEncoder.distancePerPulse = properties.driveEncoderDistancePerPulse.to(SI.METRE).value.toDouble()
    driveRightEncoder.distancePerPulse = properties.driveEncoderDistancePerPulse.to(SI.METRE).value.toDouble()
  }

  override val leftVelocity: Quantity<Speed>
    get() = Quantities.getQuantity(driveLeftEncoder.rate, SI.METRE_PER_SECOND)
  override val rightVelocity: Quantity<Speed>
    get() = Quantities.getQuantity(driveRightEncoder.rate, SI.METRE_PER_SECOND)
  override val velocity: Quantity<Speed>
    get() {
      val wheelSpeeds = DifferentialDriveWheelSpeeds(
        leftVelocity.to(SI.METRE_PER_SECOND).value.toDouble(),
        rightVelocity.to(SI.METRE_PER_SECOND).value.toDouble()
      )
      val chassisSpeeds = kinematics.toChassisSpeeds(wheelSpeeds)
      return Quantities.getQuantity(chassisSpeeds.vxMetersPerSecond, SI.METRE_PER_SECOND)
    }
  override val angularVelocity: Quantity<AngularSpeed>
    get() {
      val wheelSpeeds = DifferentialDriveWheelSpeeds(
        leftVelocity.to(SI.METRE_PER_SECOND).value.toDouble(),
        rightVelocity.to(SI.METRE_PER_SECOND).value.toDouble()
      )
      val chassisSpeeds = kinematics.toChassisSpeeds(wheelSpeeds)
      return Quantities.getQuantity(chassisSpeeds.omegaRadiansPerSecond, SI.RADIAN_PER_SECOND)
    }
  override var lowGear: Boolean
    get() = shifter.get() == properties.shiftLowGearValue
    set(value) = shifter.set(if (value) properties.shiftLowGearValue else properties.shiftHighGearValue)

  private val moveRateLimiter = VariableSlewRateLimiter()
  private val turnRateLimiter = VariableSlewRateLimiter()
  private val accelerationLimiter = VariableSlewRateLimiter()
  private val angularAccelerationLimiter = VariableSlewRateLimiter()

  private var driveOutput = {
    driveFrontLeft.set(0.0)
    driveFrontRight.set(0.0)
  }

  override fun arcadeDrive(move: Double, turn: Double) {
    driveOutput = {
      driveFrontLeft.set(ControlMode.PercentOutput, move - turn)
      driveFrontRight.set(ControlMode.PercentOutput, move + turn)
    }
  }

  override fun arcadeDrive(move: Double, turn: Double, moveRateLimit: Double, turnRateLimit: Double) {
    val newMove = moveRateLimiter.calculate(move, moveRateLimit)
    val newTurn = turnRateLimiter.calculate(turn, turnRateLimit)
    arcadeDrive(newMove, newTurn)
  }

  override fun velocityArcadeDrive(speed: Quantity<Speed>, angularSpeed: Quantity<AngularSpeed>) {
    val chassisSpeeds = ChassisSpeeds(
      speed.to(SI.METRE_PER_SECOND).value.toDouble(),
      0.0,
      angularSpeed.to(SI.RADIAN_PER_SECOND).value.toDouble()
    )
    val wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds)
    val leftVoltageFF = driveLeftFeedforward.calculate(wheelSpeeds.leftMetersPerSecond)
    val rightVoltageFF = driveRightFeedforward.calculate(wheelSpeeds.rightMetersPerSecond)
    driveOutput = {
      driveFrontLeft.setVoltage(leftVoltageFF)
      driveFrontRight.setVoltage(rightVoltageFF)
    }
  }

  override fun velocityArcadeDrive(
    xSpeed: Quantity<Speed>,
    zRotation: Quantity<AngularSpeed>,
    accelerationLimit: Quantity<Acceleration>,
    angularAccelerationLimit: Quantity<AngularAcceleration>
  ) {
    val newXSpeed = accelerationLimiter.calculate(
      xSpeed.to(SI.METRE_PER_SECOND).value.toDouble(),
      accelerationLimit.to(SI.METRE_PER_SQUARE_SECOND).value.toDouble()
    )
    val newZRotation = angularAccelerationLimiter.calculate(
      zRotation.to(SI.RADIAN_PER_SECOND).value.toDouble(),
      angularAccelerationLimit.to(SI.RADIAN_PER_SQUARE_SECOND).value.toDouble()
    )
    velocityArcadeDrive(
      Quantities.getQuantity(newXSpeed, SI.METRE_PER_SECOND),
      Quantities.getQuantity(newZRotation, SI.RADIAN_PER_SECOND)
    )
  }

  override fun periodic() {
    driveOutput()
  }
}