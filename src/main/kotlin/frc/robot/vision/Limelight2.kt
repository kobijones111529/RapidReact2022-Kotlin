package frc.robot.vision

import edu.wpi.first.networktables.NetworkTableInstance
import frc.robot.utils.div
import frc.robot.utils.plus
import frc.robot.utils.tan
import systems.uom.common.USCustomary
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Angle
import javax.measure.quantity.Length

object Limelight2 {
    private val networkTable = NetworkTableInstance.getDefault().getTable("limelight")
    private val hasTargetEntry = networkTable.getEntry("tv")
    private val targetXOffsetEntry = networkTable.getEntry("tx")
    private val targetYOffsetEntry = networkTable.getEntry("ty")

    private const val pixelWidth: Int = 320
    private const val pixelHeight: Int = 320
    private val fovX: Quantity<Angle> = Quantities.getQuantity(29.8 * 2, USCustomary.DEGREE_ANGLE)
    private val fovY: Quantity<Angle> = Quantities.getQuantity(24.85 * 2, USCustomary.DEGREE_ANGLE)

    val hasTarget: Boolean
        get() = hasTargetEntry.getNumber(0).toInt() == 1
    val targetXOffset: Quantity<Angle>?
        get() = if (hasTarget) Quantities.getQuantity(
            targetXOffsetEntry.getDouble(0.0),
            USCustomary.DEGREE_ANGLE
        ) else null
    val targetYOffset: Quantity<Angle>?
        get() = if (hasTarget) Quantities.getQuantity(
            targetYOffsetEntry.getDouble(0.0),
            USCustomary.DEGREE_ANGLE
        ) else null

    fun getTargetDistance(cameraAngle: Quantity<Angle>, targetHeight: Quantity<Length>): Quantity<Length>? {
        targetYOffset?.let {
            return targetHeight / tan(cameraAngle + it)
        } ?: return null
    }
}