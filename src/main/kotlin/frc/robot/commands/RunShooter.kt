package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import frc.robot.subsystems.Shooter
import si.uom.quantity.AngularSpeed
import javax.measure.Quantity

/**
 * Run the shooter
 */
fun runShooter(shooter: Shooter, speed: () -> Double): Command {
    return FunctionalCommand({ }, { shooter.setOutput(speed()) }, { shooter.setOutput(0.0) }, { false }, shooter)
}