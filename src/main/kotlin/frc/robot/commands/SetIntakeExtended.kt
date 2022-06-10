package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import frc.robot.subsystems.Intake

/**
 * Sets the extended/retracted state of the intake
 */
fun setIntakeExtended(intake: Intake, wantsExtended: Boolean): Command {
    return InstantCommand({ intake.extended = wantsExtended }, intake)
}