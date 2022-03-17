package frc.robot

import edu.wpi.first.wpilibj.DoubleSolenoid.Value
import edu.wpi.first.wpilibj.PneumaticsModuleType
import frc.robot.subsystems.Magazine
import frc.robot.subsystems.physical.PhysicalDrivetrain
import frc.robot.subsystems.physical.PhysicalMagazine
import frc.robot.subsystems.physical.PhysicalShooter
import frc.robot.utils.FeedforwardData
import frc.robot.utils.PIDData
import si.uom.SI
import tech.units.indriya.quantity.Quantities

/**
 * Constant properties and configuration data for the robot
 */
object Constants {
  // CAN
  private const val DRIVE_FRONT_LEFT_ID = 1
  private const val DRIVE_FRONT_RIGHT_ID = 2
  private const val DRIVE_BACK_LEFT_ID = 3
  private const val DRIVE_BACK_RIGHT_ID = 4
  private const val MAGAZINE_MOTOR_ID = 5
  private const val SHOOTER_MOTOR_ID = 6

  // DIO
  private const val DRIVE_LEFT_ENCODER_CHANNEL_A = 0
  private const val DRIVE_LEFT_ENCODER_CHANNEL_B = 1
  private const val DRIVE_RIGHT_ENCODER_CHANNEL_A = 2
  private const val DRIVE_RIGHT_ENCODER_CHANNEL_B = 3

  // Pneumatics
  private val PNEUMATICS_MODULE_TYPE = PneumaticsModuleType.CTREPCM
  private const val DRIVE_SHIFT_FORWARD_CHANNEL = 0;
  private const val DRIVE_SHIFT_REVERSE_CHANNEL = 1;

  /**
   * Drivetrain properties
   */
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
    FeedforwardData(0.0, 0.0, 0.0),
    FeedforwardData(0.0, 0.0, 0.0),
    Quantities.getQuantity(0, SI.METRE),
    Quantities.getQuantity(0, SI.METRE)
  )

  /**
   * Magazine properties
   */
  val MAGAZINE_PROPERTIES: PhysicalMagazine.Properties = PhysicalMagazine.Properties(MAGAZINE_MOTOR_ID)

  /**
   * Shooter properties
   */
  val SHOOTER_PROPERTIES: PhysicalShooter.Properties = PhysicalShooter.Properties(
    SHOOTER_MOTOR_ID,
    FeedforwardData(0.0, 0.0, 0.0),
    PIDData(0.0, 0.0, 0.0),
    2048,
    1.0
  )
}