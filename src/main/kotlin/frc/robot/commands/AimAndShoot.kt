package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.onlyWhen
import edu.wpi.first.wpilibj2.command.repeatedly
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Magazine
import frc.robot.subsystems.Shooter
import frc.robot.utils.compareTo
import frc.robot.utils.diff
import si.uom.SI
import si.uom.quantity.AngularSpeed
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Angle

fun aimAndShoot(
    drivetrain: Drivetrain,
    magazine: Magazine,
    shooter: Shooter,
    targetOffset: () -> Quantity<Angle>?,
    targetOffsetTolerance: Quantity<Angle>,
    magazineSpeed: () -> Double,
    shootSpeed: () -> Quantity<AngularSpeed>?,
    shootSpeedTolerance: Quantity<AngularSpeed>,
    backRunMagazineTime: Double
): Command {
    val canShoot =
        { targetOffset()?.let { diff(it, Quantities.getQuantity(0, SI.RADIAN)) < targetOffsetTolerance } ?: false }
    return autoAim(drivetrain, targetOffset, targetOffsetTolerance).alongWith(
        shoot(
            magazine,
            shooter,
            magazineSpeed,
            shootSpeed,
            shootSpeedTolerance,
            backRunMagazineTime
        ).onlyWhen(canShoot).repeatedly()
    )
}