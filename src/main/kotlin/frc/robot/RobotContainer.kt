package frc.robot

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.RunCommand
import edu.wpi.first.wpilibj2.command.button.Trigger
import frc.robot.commands.ArcadeDriveCommand
import frc.robot.commands.Commands
import frc.robot.io.ControlBoard
import frc.robot.io.Inputs
import frc.robot.io.Triggers
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Intake
import frc.robot.subsystems.physical.PhysicalDrivetrain
import frc.robot.subsystems.Magazine
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.dummy.DummyDrivetrain
import frc.robot.subsystems.dummy.DummyIntake
import frc.robot.subsystems.dummy.DummyMagazine
import frc.robot.subsystems.dummy.DummyShooter
import frc.robot.subsystems.physical.PhysicalIntake
import frc.robot.subsystems.physical.PhysicalMagazine
import frc.robot.subsystems.physical.PhysicalShooter
import java.util.EnumMap

class RobotContainer {
  private val drivetrainEnabled = true
  private val intakeEnabled = true
  private val magazineEnabled = true
  private val shooterEnabled = true

  private val drivetrain: Drivetrain =
    if (drivetrainEnabled) PhysicalDrivetrain(Constants.DRIVETRAIN_PROPERTIES) else DummyDrivetrain()
  private val intake: Intake = if (intakeEnabled) PhysicalIntake(Constants.INTAKE_PROPERTIES) else DummyIntake()
  private val magazine: Magazine =
    if (magazineEnabled) PhysicalMagazine(Constants.MAGAZINE_PROPERTIES) else DummyMagazine()
  private val shooter: Shooter = if (shooterEnabled) PhysicalShooter(Constants.SHOOTER_PROPERTIES) else DummyShooter()

  private val controlBoard = ControlBoard()

  private val inputMap: EnumMap<Inputs, () -> Number> = EnumMap(Inputs::class.java)
  private val triggerMap: EnumMap<Triggers, Trigger> = EnumMap(Triggers::class.java)

  val autonomousCommand: Command? = null

  init {
    inputMap[Inputs.MOVE] = { controlBoard.xboxController.leftY }
    inputMap[Inputs.TURN] = { controlBoard.xboxController.rightX }

    drivetrain.defaultCommand =
      ArcadeDriveCommand(
        drivetrain,
        { inputMap[Inputs.MOVE]?.invoke()?.toDouble() ?: 0.0 },
        { inputMap[Inputs.TURN]?.invoke()?.toDouble() ?: 0.0 })

    triggerMap[Triggers.TOGGLE_INTAKE_EXTENDED]?.whenActive(Commands.toggleIntakeExtended(intake))
    triggerMap[Triggers.RUN_INTAKE]?.whileActiveContinuous(Commands.runIntake(intake) { Constants.INTAKE_RUN_SPEED })
    triggerMap[Triggers.RUN_MAGAZINE]?.whileActiveContinuous(Commands.runMagazine(magazine) { Constants.MAGAZINE_RUN_SPEED })
  }
}