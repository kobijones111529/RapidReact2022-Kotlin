package frc.robot.subsystems.physical

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.InvertType
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX
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

    private val driveFrontLeft = WPI_TalonFX(properties.driveFrontLeftID)
    private val driveFrontRight = WPI_TalonFX(properties.driveFrontRightID)
    private val driveBackLeft = WPI_TalonFX(properties.driveBackLeftID)
    private val driveBackRight = WPI_TalonFX(properties.driveBackRightID)

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
        val allMotorConfig = TalonFXConfiguration()

        driveFrontLeft.configAllSettings(allMotorConfig)
        driveFrontRight.configAllSettings(allMotorConfig)
        driveBackLeft.configAllSettings(allMotorConfig)
        driveBackRight.configAllSettings(allMotorConfig)

        driveBackLeft.follow(driveFrontLeft)
        driveBackRight.follow(driveFrontRight)

        driveFrontLeft.setInverted(InvertType.None)
        driveFrontRight.setInverted(InvertType.InvertMotorOutput)
        driveBackLeft.setInverted(InvertType.FollowMaster)
        driveBackRight.setInverted(InvertType.FollowMaster)

        driveLeftEncoder.distancePerPulse = properties.driveEncoderDistancePerPulse.to(SI.METRE).value.toDouble()
        driveRightEncoder.distancePerPulse = properties.driveEncoderDistancePerPulse.to(SI.METRE).value.toDouble()
    }

    override val leftSpeed: Quantity<Speed>
        get() = Quantities.getQuantity(driveLeftEncoder.rate, SI.METRE_PER_SECOND)
    override val rightSpeed: Quantity<Speed>
        get() = Quantities.getQuantity(driveRightEncoder.rate, SI.METRE_PER_SECOND)
    override val speed: Quantity<Speed>
        get() {
            val wheelSpeeds = DifferentialDriveWheelSpeeds(
                leftSpeed.to(SI.METRE_PER_SECOND).value.toDouble(),
                rightSpeed.to(SI.METRE_PER_SECOND).value.toDouble()
            )
            val chassisSpeeds = kinematics.toChassisSpeeds(wheelSpeeds)
            return Quantities.getQuantity(chassisSpeeds.vxMetersPerSecond, SI.METRE_PER_SECOND)
        }
    override val angularSpeed: Quantity<AngularSpeed>
        get() {
            val wheelSpeeds = DifferentialDriveWheelSpeeds(
                leftSpeed.to(SI.METRE_PER_SECOND).value.toDouble(),
                rightSpeed.to(SI.METRE_PER_SECOND).value.toDouble()
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
        driveFrontLeft.set(ControlMode.Disabled, 0.0)
        driveFrontRight.set(ControlMode.Disabled, 0.0)
    }

    override fun neutralOutput() {
        driveOutput = {
            driveFrontLeft.set(ControlMode.Disabled, 0.0)
            driveFrontRight.set(ControlMode.Disabled, 0.0)
        }
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
        speed: Quantity<Speed>,
        angularSpeed: Quantity<AngularSpeed>,
        accelerationLimit: Quantity<Acceleration>,
        angularAccelerationLimit: Quantity<AngularAcceleration>
    ) {
        val newSpeed = accelerationLimiter.calculate(
            speed.to(SI.METRE_PER_SECOND).value.toDouble(),
            accelerationLimit.to(SI.METRE_PER_SQUARE_SECOND).value.toDouble()
        )
        val newAngularSpeed = angularAccelerationLimiter.calculate(
            angularSpeed.to(SI.RADIAN_PER_SECOND).value.toDouble(),
            angularAccelerationLimit.to(SI.RADIAN_PER_SQUARE_SECOND).value.toDouble()
        )
        velocityArcadeDrive(
            Quantities.getQuantity(newSpeed, SI.METRE_PER_SECOND),
            Quantities.getQuantity(newAngularSpeed, SI.RADIAN_PER_SECOND)
        )
    }

    override fun periodic() {
        driveOutput()
    }
}