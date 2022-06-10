package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import frc.robot.subsystems.Drivetrain

fun shift(drivetrain: Drivetrain): Command {
    return InstantCommand({ drivetrain.lowGear = !drivetrain.lowGear }, drivetrain)
}

fun shift(drivetrain: Drivetrain, wantsLowGear: Boolean): Command {
    return InstantCommand({ drivetrain.lowGear = wantsLowGear }, drivetrain)
}