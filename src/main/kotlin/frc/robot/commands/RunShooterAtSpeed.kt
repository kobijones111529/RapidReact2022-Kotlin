package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import frc.robot.subsystems.Shooter
import si.uom.quantity.AngularSpeed
import javax.measure.Quantity

/**
 * Run the shooter using velocity control
 */
fun runShooterAtSpeed(shooter: Shooter, speed: () -> Quantity<AngularSpeed>?): Command {
  return FunctionalCommand(
    { },
    { speed()?.let { shooter.setSpeed(it) } },
    { shooter.setOutput(0.0) },
    { false },
    shooter
  )
}