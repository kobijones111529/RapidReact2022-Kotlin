package frc.robot.vision

import kotlin.math.tan

class Limelight {
    private val pixelWidth: Int = TODO()
    private val pixelHeight: Int = TODO()
    private val fovX: Double = TODO()
    private val fovY: Double = TODO()

    private val hasTarget: Boolean
    get() = TODO()
    private val targetXPixel: Int
    get() = TODO()
    private val targetYPixel: Int
    get() = TODO()
    private val targetWidthPixels: Int
    get() = TODO()
    private val targetHeightPixels: Int
    get() = TODO()

    val targetX: Double
    get() = pixelXToAngle(targetXPixel)
    val targetY: Double
    get() = pixelYToAngle(targetYPixel)
    val targetSpanX: Double
    get() = pixelWidthToAngle(targetWidthPixels)
    val targetSpanY: Double
    get() = pixelHeightToAngle(targetHeightPixels)

    val targetOffsetX: Double
    get() = targetX + targetSpanX / 2
    val targetOffsetY: Double
    get() = targetY + targetSpanY / 2

    private fun pixelXToAngle(x: Int): Double {
        return (x.toDouble() / pixelWidth - 0.5) * fovX
    }

    private fun pixelYToAngle(y: Int): Double {
        return (y.toDouble() / pixelHeight - 0.5) * fovY
    }

    private fun pixelWidthToAngle(width: Int): Double {
        return width.toDouble() / pixelWidth * fovX
    }

    private fun pixelHeightToAngle(height: Int): Double {
        return height.toDouble() / pixelHeight * fovY
    }

    fun getDistanceFromWidth(actualWidth: Double): Double {
        return actualWidth / tan(targetSpanX / 2) / 2
    }

    fun getDistanceFromHeight(actualHeight: Double): Double {
        return actualHeight / tan(targetSpanY / 2) / 2
    }
}