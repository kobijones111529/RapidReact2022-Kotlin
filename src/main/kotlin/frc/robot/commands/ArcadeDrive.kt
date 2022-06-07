package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import frc.robot.subsystems.Drivetrain

/**
 * Simple arcade drive
 */
fun arcadeDrive(drivetrain: Drivetrain, move: () -> Double, turn: () -> Double): Command {
  return FunctionalCommand(
    { },
    { drivetrain.arcadeDrive(move(), turn()) },
    { drivetrain.arcadeDrive(0.0, 0.0) },
    { false },
    drivetrain
  )
}

/**
 * Simple arcade drive using slew rate limiting
 */
fun arcadeDrive(
  drivetrain: Drivetrain,
  move: () -> Double,
  turn: () -> Double,
  moveRateLimit: () -> Double,
  turnRateLimit: () -> Double
): Command {
  return FunctionalCommand(
    { },
    { drivetrain.arcadeDrive(move(), turn(), moveRateLimit(), turnRateLimit()) },
    { drivetrain.arcadeDrive(0.0, 0.0) },
    { false },
    drivetrain
  )
}