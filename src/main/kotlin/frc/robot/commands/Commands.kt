package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.RunCommand
import frc.robot.subsystems.*
import java.util.function.Supplier

object Commands {
  fun arcadeDrive(drivetrain: Drivetrain, move: () -> Double, turn: () -> Double): Command {
    return FunctionalCommand(
      { },
      { drivetrain.arcadeDrive(move(), turn()) },
      { drivetrain.arcadeDrive(0.0, 0.0) },
      { false },
      drivetrain
    )
  }

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

  fun setIntakeExtended(intake: Intake, wantsExtended: Boolean): Command {
    return InstantCommand({ intake.extended = wantsExtended }, intake)
  }

  fun toggleIntakeExtended(intake: Intake): Command {
    return InstantCommand({ intake.extended = !intake.extended }, intake)
  }

  fun runIntake(intake: Intake, output: () -> Double): Command {
    return FunctionalCommand({ }, { intake.setOutput(output()) }, { intake.setOutput(0.0) }, { false }, intake)
  }

  fun runMagazine(magazine: Magazine, output: () -> Double): Command {
    return FunctionalCommand({ }, { magazine.setOutput(output()) }, { magazine.setOutput(0.0) }, { false }, magazine)
  }

  fun runShooter(shooter: Shooter, output: () -> Double): Command {
    return FunctionalCommand({ }, { shooter.setOutput(output()) }, { shooter.setOutput(0.0) }, { false }, shooter)
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