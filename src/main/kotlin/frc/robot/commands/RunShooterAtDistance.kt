package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Constants
import frc.robot.subsystems.Shooter
import frc.robot.utils.InterpolatingQuantity
import systems.uom.common.USCustomary
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Length

fun runShooterAtDistance(shooter: Shooter, distance: () -> Quantity<Length>): Command {
  return RunShooterAtDistance(shooter, distance)
}

private class RunShooterAtDistance(private val shooter: Shooter, private val distance: () -> Quantity<Length>) :
  CommandBase() {
  init {
    addRequirements(shooter)
  }

  override fun execute() {
    shooter.setSpeed(
      Constants.SHOOTER_MAP[InterpolatingQuantity(distance())]?.value ?: Quantities.getQuantity(
        0,
        USCustomary.REVOLUTION_PER_MINUTE
      )
    )
  }

  override fun end(interrupted: Boolean) {
    shooter.setSpeed(Quantities.getQuantity(0, USCustomary.REVOLUTION_PER_MINUTE))
  }
}