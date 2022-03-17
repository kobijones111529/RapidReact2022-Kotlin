package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.SimpleDrivetrain
import java.util.function.DoubleSupplier

@Suppress("unused")
class ArcadeDriveCommand(
  private val drivetrain: Drivetrain,
  private val moveSupplier: DoubleSupplier,
  private val turnSupplier: DoubleSupplier
) : CommandBase() {
  init {
    addRequirements(drivetrain)
  }

  override fun execute() {
    drivetrain.arcadeDrive(moveSupplier.asDouble, turnSupplier.asDouble)
  }
}