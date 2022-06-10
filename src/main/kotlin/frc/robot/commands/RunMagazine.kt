package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import frc.robot.subsystems.Magazine

/**
 * Run the magazine
 */
fun runMagazine(magazine: Magazine, speed: () -> Double): Command {
    return FunctionalCommand({ }, { magazine.setOutput(speed()) }, { magazine.setOutput(0.0) }, { false }, magazine)
}