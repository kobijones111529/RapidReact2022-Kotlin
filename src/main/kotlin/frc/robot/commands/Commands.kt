package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.RunCommand
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.EncoderShooter
import frc.robot.subsystems.Magazine
import frc.robot.subsystems.Shooter
import java.util.function.Supplier

object Commands {
  fun arcadeDrive(drivetrain: Drivetrain, move: () -> Double, turn: () -> Double): Command {
    return RunCommand({ drivetrain.arcadeDrive(move(), turn()) }, drivetrain)
  }

  fun arcadeDrive(
    drivetrain: Drivetrain,
    move: () -> Double,
    turn: () -> Double,
    moveRateLimit: () -> Double,
    turnRateLimit: () -> Double
  ): Command {
    return RunCommand({ drivetrain.arcadeDrive(move(), turn(), moveRateLimit(), turnRateLimit()) }, drivetrain)
  }

  fun shootFullAuto(
    magazine: Magazine,
    shooter: Shooter,
    distanceSupplier: Supplier<Double?>,
    angleOffsetSupplier: Supplier<Double?>,
    shooterDistanceToRPM: (distance: Double) -> Double
  ): Command {
    return ShootFullAuto(magazine, shooter, distanceSupplier, angleOffsetSupplier, shooterDistanceToRPM)
  }
}