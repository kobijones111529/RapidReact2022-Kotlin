package frc.robot.utils

import si.uom.SI
import si.uom.quantity.AngularAcceleration
import si.uom.quantity.AngularSpeed
import systems.uom.common.USCustomary
import javax.measure.MetricPrefix

/**
 * Defines custom units
 */
object CustomUnits {
    val REVOLUTION_PER_100_MILLISECOND: javax.measure.Unit<AngularSpeed> =
        SI.REVOLUTION.divide(MetricPrefix.MILLI(SI.SECOND)).asType(AngularSpeed::class.java)
    val REVOLUTION_PER_MINUTE_PER_SECOND: javax.measure.Unit<AngularAcceleration> =
        USCustomary.REVOLUTION_PER_MINUTE.divide(SI.SECOND).asType(AngularAcceleration::class.java)
}