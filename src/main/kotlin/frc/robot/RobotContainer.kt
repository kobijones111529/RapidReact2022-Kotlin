package frc.robot

import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.EntryNotification
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.button.Button
import edu.wpi.first.wpilibj2.command.button.Trigger
import frc.robot.commands.*
import frc.robot.io.Inputs
import frc.robot.io.Limelight
import frc.robot.io.PhysicalLimelight
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Magazine
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.dummy.DummyDrivetrain
import frc.robot.subsystems.dummy.DummyIntake
import frc.robot.subsystems.dummy.DummyMagazine
import frc.robot.subsystems.dummy.DummyShooter
import frc.robot.subsystems.physical.PhysicalDrivetrain
import frc.robot.subsystems.physical.PhysicalIntake
import frc.robot.subsystems.physical.PhysicalMagazine
import frc.robot.subsystems.physical.PhysicalShooter
import frc.robot.utils.CameraUtils
import frc.robot.utils.InterpolatingQuantity
import frc.robot.vision.Limelight2
import systems.uom.common.USCustomary
import tech.units.indriya.quantity.Quantities
import java.util.*

class RobotContainer {
  private val networkTable = NetworkTableInstance.getDefault().getTable(javaClass.simpleName)

  // Temp
  private val shooterTuningModeEntry = networkTable.getEntry("Set shooter tuning mode")
  private val shooterSpeedEntry = networkTable.getEntry("Set shooter rpm")

  private val limelight: Limelight = PhysicalLimelight(Constants.LIMELIGHT_TABLE_NAME)

  private var drivetrain: Drivetrain =
    if (Constants.DRIVETRAIN_ENABLED) PhysicalDrivetrain(Constants.DRIVETRAIN_PROPERTIES) else DummyDrivetrain()
  private var intake: Intake =
    if (Constants.INTAKE_ENABLED) PhysicalIntake(Constants.INTAKE_PROPERTIES) else DummyIntake()
  private var magazine: Magazine =
    if (Constants.MAGAZINE_ENABLED) PhysicalMagazine(Constants.MAGAZINE_PROPERTIES) else DummyMagazine()
  private var shooter: Shooter =
    if (Constants.SHOOTER_ENABLED) PhysicalShooter(Constants.SHOOTER_PROPERTIES) else DummyShooter()

  val autonomousCommand: Command? = null

  init {
//    shooterTuningModeEntry.addListener({ e: EntryNotification ->
//      shooter.defaultCommand = if (e.value.boolean) Commands.runShooterAtSpeed(shooter) {
//        Quantities.getQuantity(
//          shooterSpeedEntry.getDouble(0.0), USCustomary.REVOLUTION_PER_MINUTE
//        )
//      } else null
//    }, EntryListenerFlags.kUpdate)

    drivetrain.defaultCommand = arcadeDrive(drivetrain, Inputs::move, Inputs::turn)

    Inputs.shiftLowGear.whenActive(shift(drivetrain, true))
    Inputs.shiftHighGear.whenActive(shift(drivetrain, false))
    Inputs.shift.whenActive(shift(drivetrain))
    Inputs.extendIntake.whenActive(setIntakeExtended(intake, true))
    Inputs.retractIntake.whenActive(setIntakeExtended(intake, false))
    Inputs.toggleIntake.whenActive(toggleIntakeExtended(intake))
    Inputs.runIntakeIn.whileActiveContinuous(runIntake(intake) { Constants.INTAKE_RUN_SPEED })
    Inputs.runIntakeOut.whileActiveContinuous(runIntake(intake) { -Constants.INTAKE_RUN_SPEED })
    Inputs.runMagazineIn.whileActiveContinuous(runMagazine(magazine) { Constants.MAGAZINE_RUN_SPEED })
    Inputs.runMagazineOut.whileActiveContinuous(runMagazine(magazine) { -Constants.MAGAZINE_RUN_SPEED })
    Inputs.runShooter.whileActiveContinuous(runShooter(shooter) { Constants.SHOOTER_RUN_SPEED })
    Inputs.autoAim.whileActiveContinuous(
      autoAim(
        drivetrain,
        { limelight.targetOffset?.x },
        Constants.TARGET_OFFSET_TOLERANCE
      )
    )
    Inputs.autoShootHigh.whileActiveContinuous(
      aimAndShoot(
        drivetrain,
        magazine,
        shooter,
        { limelight.targetOffset?.x },
        Constants.TARGET_OFFSET_TOLERANCE,
        { Constants.MAGAZINE_RUN_SPEED },
        {
          CameraUtils.distance(
            Constants.LIMELIGHT_MOUNT_ANGLE,
            Constants.VISION_TARGET_HEIGHT,
            limelight.targetOffset?.y
          )?.let {
            InterpolatingQuantity(it)
          }?.let {
            Constants.SHOOTER_MAP[it]?.value
          }
        },
        Constants.SHOOTER_SPEED_TOLERANCE,
        Constants.MAGAZINE_BACK_RUN_TIME
      )
    )

//    triggerMap[Triggers.AUTO_AIM]?.whileActiveContinuous(Commands.autoAim(drivetrain, { Limelight2.targetXOffset }))
//    triggerMap[Triggers.SHOOT_LOW]?.whileActiveContinuous(
//      Commands.shoot(
//        magazine,
//        shooter,
//        { Constants.MAGAZINE_RUN_SPEED },
//        {
//          Constants.SHOOTER_MAP[InterpolatingQuantity(Constants.SHOOT_LOW_DISTANCE)]?.value ?: Quantities.getQuantity(
//            0,
//            USCustomary.REVOLUTION_PER_MINUTE
//          )
//        },
//        Constants.SHOOTER_SPEED_TOLERANCE
//      )
//    )
//    triggerMap[Triggers.SHOOT_HIGH]?.whileActiveContinuous(
//      Commands.shoot(
//        magazine,
//        shooter,
//        { Constants.MAGAZINE_RUN_SPEED },
//        {
//          Limelight2.getTargetDistance(Constants.LIMELIGHT_MOUNT_ANGLE, Constants.VISION_TARGET_HEIGHT)?.let {
//            Constants.SHOOTER_MAP[InterpolatingQuantity(it)]?.value
//          }
//        },
//        Constants.SHOOTER_SPEED_TOLERANCE
//      )
//    )
  }
}