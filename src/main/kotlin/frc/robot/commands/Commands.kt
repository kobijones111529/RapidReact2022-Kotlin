package frc.robot.commands

import edu.wpi.first.wpilibj2.command.*
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Magazine
import frc.robot.subsystems.Shooter
import frc.robot.utils.compareTo
import frc.robot.utils.diff
import si.uom.SI
import si.uom.quantity.AngularSpeed
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Angle

/**
 * Utility class for convenient command construction
 */
object Commands {
  fun aimAndShoot(
    drivetrain: Drivetrain,
    magazine: Magazine,
    shooter: Shooter,
    targetOffset: () -> Quantity<Angle>?,
    offsetTolerance: Quantity<Angle>,
    magazineSpeed: () -> Double,
    shootSpeed: () -> Quantity<AngularSpeed>,
    speedTolerance: Quantity<AngularSpeed>
  ): Command {
    // Shooter is aimed
    val canShoot: () -> Boolean = {
      targetOffset.invoke()?.let {
        diff(it, Quantities.getQuantity(0, SI.RADIAN)) < offsetTolerance
      } ?: false
    }

    return autoAim(drivetrain, targetOffset, offsetTolerance).alongWith(
      RepeatCommand(
        WaitUntilCommand(canShoot).andThen(
          shoot(
            magazine,
            shooter,
            magazineSpeed,
            shootSpeed,
            speedTolerance,
            0.5
          ).withInterrupt { !canShoot.invoke() })
      )
    )
  }
}