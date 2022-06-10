package frc.robot.io

import javax.measure.Quantity
import javax.measure.quantity.Angle

data class Point<T>(
    val x: T,
    val y: T
)

data class Size<T>(
    val width: T,
    val height: T
)

interface Limelight {
    enum class LEDMode(val id: Int) {
        AUTO(0),
        FORCE_OFF(1),
        FORCE_BLINK(2),
        FORCE_ON(3);

        companion object {
            private val map = values().associateBy { it.id }

            operator fun get(id: Int): LEDMode? {
                return map[id]
            }
        }
    }

    enum class CamMode(val id: Int) {
        VISION(0),
        DRIVER(1);

        companion object {
            private val map = values().associateBy { it.id }

            operator fun get(id: Int): CamMode? {
                return map[id]
            }
        }
    }

    // Data
    val hasTarget: Boolean
    val targetOffset: Point<Quantity<Angle>>?
    val targetArea: Double?
    val skew: Quantity<Angle>?
//  val fittedBoundingBox: Size<Quantity<Angle>>?
//  val roughBoundingBox: Size<Quantity<Angle>>?

    // Camera controls
    var ledMode: LEDMode
    var camMode: CamMode
    var pipeline: Int

    // Python
    val pythonIn: Array<Number>
    var pythonOut: Array<Number>
}

object LimelightAPIKeys {
    val hasTarget = "tv"
    val targetOffsetX = "tx"
    val targetOffsetY = "ty"
    val targetArea = "ta"
    val skew = "ts"
    val fittedBoundingBoxShort = "tshort"
    val fittedBoundingBoxLong = "tlong"
    val roughBoundingBoxHorizontal = "thor"
    val roughBoundingBoxVertical = "tvert"
    val getPipeline = "getpipe"
    val ledMode = "ledMode"
    val camMode = "camMode"
    val setPipeline = "pipeline"
    val pythonIn = "llpython"
    val pythonOut = "llrobot"
}

data class LimelightConfig(
    val fov: Size<Quantity<Angle>>,
    val resolution: Size<Int>
)