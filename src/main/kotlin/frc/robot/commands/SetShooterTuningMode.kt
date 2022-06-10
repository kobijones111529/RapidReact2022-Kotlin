package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import frc.robot.subsystems.Shooter

fun setShooterTuningMode(shooter: Shooter, wantsTuningMode: Boolean): Command {
    return InstantCommand({ shooter.defaultCommand = if (wantsTuningMode) TODO() else null }, shooter)
}