package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.onlyWhen
import edu.wpi.first.wpilibj2.command.repeatedly
import frc.robot.subsystems.Magazine
import frc.robot.subsystems.Shooter
import frc.robot.utils.compareTo
import frc.robot.utils.diff
import si.uom.quantity.AngularSpeed
import javax.measure.Quantity

fun shoot(
    magazine: Magazine,
    shooter: Shooter,
    magazineSpeed: () -> Double,
    shootSpeed: () -> Quantity<AngularSpeed>?,
    tolerance: Quantity<AngularSpeed>,
    backRunMagazineTime: Double
): Command {
    val canShoot = { shootSpeed()?.let { diff(it, shooter.speed) <= tolerance } ?: false }
    val backRunMagazine = runMagazine(magazine) { -magazineSpeed() }.withTimeout(backRunMagazineTime)
    return runShooterAtSpeed(shooter, shootSpeed).alongWith(
        runMagazine(magazine, magazineSpeed).onlyWhen(canShoot).andThen(backRunMagazine).repeatedly()
    )
}