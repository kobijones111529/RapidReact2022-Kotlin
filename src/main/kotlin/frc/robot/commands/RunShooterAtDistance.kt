package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.EncoderShooter
import frc.robot.subsystems.Shooter
import java.util.function.DoubleSupplier

class RunShooterAtDistance(private val shooter: Shooter, private val distance: () -> Double) : CommandBase() {
  init {
    addRequirements(shooter)
  }

  override fun execute() {
    TODO("Figure this out")
  }
}