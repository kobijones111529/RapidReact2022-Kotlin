package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import frc.robot.subsystems.Intake

/**
 * Run the intake
 */
fun runIntake(intake: Intake, speed: () -> Double): Command {
    return FunctionalCommand({ }, { intake.setOutput(speed()) }, { intake.setOutput(0.0) }, { false }, intake)
}