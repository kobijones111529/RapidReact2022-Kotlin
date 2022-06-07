package frc.robot

import edu.wpi.first.wpilibj.DoubleSolenoid.Value
import edu.wpi.first.wpilibj.PneumaticsModuleType
import frc.robot.io.LimelightConfig
import frc.robot.io.Size
import frc.robot.subsystems.physical.PhysicalDrivetrain
import frc.robot.subsystems.physical.PhysicalIntake
import frc.robot.subsystems.physical.PhysicalMagazine
import frc.robot.subsystems.physical.PhysicalShooter
import frc.robot.utils.SimpleMotorFeedforwardConstants
import frc.robot.utils.InterpolatingQuantity
import frc.robot.utils.InterpolatingTreeMap
import frc.robot.utils.PIDConstants
import si.uom.SI
import si.uom.quantity.AngularSpeed
import systems.uom.common.USCustomary
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Angle
import javax.measure.quantity.Length

/**
 * Constant properties and configuration data for the robot
 */
object Constants {
  // NetworkTables
  val LIMELIGHT_TABLE_NAME = "limelight"

  // Controls
  const val XBOX_PORT: Int = 0
  const val EXTREME_PORT: Int = 1
  const val BUTTON_BOX_PORT: Int = 2

  // Enable subsystems
  const val DRIVETRAIN_ENABLED: Boolean = true
  const val INTAKE_ENABLED: Boolean = true
  const val MAGAZINE_ENABLED: Boolean = true
  const val SHOOTER_ENABLED: Boolean = true
  const val CLIMBER_ENABLED: Boolean = true

  // Config
  val TARGET_OFFSET_TOLERANCE: Quantity<Angle> = TODO()
  val SHOOT_LOW_DISTANCE: Quantity<Length> = Quantities.getQuantity(0, SI.METRE)
  val SHOOTER_SPEED_TOLERANCE: Quantity<AngularSpeed> = Quantities.getQuantity(10, USCustomary.REVOLUTION_PER_MINUTE)
  val MAGAZINE_BACK_RUN_TIME: Double = 0.5
  const val INTAKE_RUN_SPEED: Double = 1.0
  const val MAGAZINE_RUN_SPEED: Double = 1.0
  const val SHOOTER_RUN_SPEED: Double = 1.0

  // Field
  val VISION_TARGET_HEIGHT: Quantity<Length> = TODO()

  // Limelight
  val LIMELIGHT_2_CONFIG = LimelightConfig(
    Size(
      Quantities.getQuantity(29.8 * 2, USCustomary.DEGREE_ANGLE),
      Quantities.getQuantity(24.85 * 2, USCustomary.DEGREE_ANGLE)
    ), Size(320, 240)
  )
  val LIMELIGHT_MOUNT_ANGLE: Quantity<Angle> = TODO()

  // CAN
  private const val DRIVE_FRONT_LEFT_ID = 1
  private const val DRIVE_FRONT_RIGHT_ID = 2
  private const val DRIVE_BACK_LEFT_ID = 3
  private const val DRIVE_BACK_RIGHT_ID = 4
  private const val INTAKE_MOTOR_ID = 5
  private const val MAGAZINE_MOTOR_ID = 6
  private const val SHOOTER_MOTOR_ID = 7

  // DIO
  private const val DRIVE_LEFT_ENCODER_CHANNEL_A = 0
  private const val DRIVE_LEFT_ENCODER_CHANNEL_B = 1
  private const val DRIVE_RIGHT_ENCODER_CHANNEL_A = 2
  private const val DRIVE_RIGHT_ENCODER_CHANNEL_B = 3

  // Pneumatics
  private val PNEUMATICS_MODULE_TYPE = PneumaticsModuleType.CTREPCM
  private const val DRIVE_SHIFT_FORWARD_CHANNEL = 0
  private const val DRIVE_SHIFT_REVERSE_CHANNEL = 1
  private const val INTAKE_EXTEND_FORWARD_CHANNEL = 2
  private const val INTAKE_EXTEND_REVERSE_CHANNEL = 3

  /**
   * Maps shooting distances to shooter speeds
   */
  val SHOOTER_MAP: InterpolatingTreeMap<InterpolatingQuantity<Length>, InterpolatingQuantity<AngularSpeed>> = mapOf(
    InterpolatingQuantity(0, SI.METRE) to InterpolatingQuantity(0, USCustomary.REVOLUTION_PER_MINUTE)
  ) as InterpolatingTreeMap<InterpolatingQuantity<Length>, InterpolatingQuantity<AngularSpeed>>

  val DRIVETRAIN_PROPERTIES: PhysicalDrivetrain.Properties = PhysicalDrivetrain.Properties(
    DRIVE_FRONT_LEFT_ID,
    DRIVE_FRONT_RIGHT_ID,
    DRIVE_BACK_LEFT_ID,
    DRIVE_BACK_RIGHT_ID,
    DRIVE_LEFT_ENCODER_CHANNEL_A,
    DRIVE_LEFT_ENCODER_CHANNEL_B,
    DRIVE_RIGHT_ENCODER_CHANNEL_A,
    DRIVE_RIGHT_ENCODER_CHANNEL_B,
    PNEUMATICS_MODULE_TYPE,
    DRIVE_SHIFT_FORWARD_CHANNEL,
    DRIVE_SHIFT_REVERSE_CHANNEL,
    Value.kForward,
    SimpleMotorFeedforwardConstants(0.0, 0.0, 0.0),
    SimpleMotorFeedforwardConstants(0.0, 0.0, 0.0),
    Quantities.getQuantity(0, SI.METRE),
    Quantities.getQuantity(0, SI.METRE)
  )
  val INTAKE_PROPERTIES: PhysicalIntake.Properties = PhysicalIntake.Properties(
    INTAKE_MOTOR_ID,
    PNEUMATICS_MODULE_TYPE,
    INTAKE_EXTEND_FORWARD_CHANNEL,
    INTAKE_EXTEND_REVERSE_CHANNEL,
    Value.kForward
  )
  val MAGAZINE_PROPERTIES: PhysicalMagazine.Properties = PhysicalMagazine.Properties(MAGAZINE_MOTOR_ID)
  val SHOOTER_PROPERTIES: PhysicalShooter.Properties = PhysicalShooter.Properties(
    SHOOTER_MOTOR_ID,
    2048,
    1.0,
    SimpleMotorFeedforwardConstants(0.0, 0.0, 0.0),
    PIDConstants(0.0, 0.0, 0.0)
  )
}