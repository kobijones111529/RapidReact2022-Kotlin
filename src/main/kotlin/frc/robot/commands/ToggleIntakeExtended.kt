package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import frc.robot.subsystems.Intake

/**
 * Toggle extended/retracted state of the intake
 */
fun toggleIntakeExtended(intake: Intake): Command {
  return InstantCommand({ intake.extended = !intake.extended }, intake)
}