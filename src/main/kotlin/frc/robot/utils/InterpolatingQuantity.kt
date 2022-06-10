package frc.robot.utils

import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity

class InterpolatingQuantity<T>(val value: Quantity<T>) : Interpolable<InterpolatingQuantity<T>>,
    InverseInterpolable<InterpolatingQuantity<T>>,
    Comparable<InterpolatingQuantity<T>> where T : Quantity<T> {

    constructor(value: Number, unit: javax.measure.Unit<T>) : this(Quantities.getQuantity(value, unit))

    override fun interpolate(upper: InterpolatingQuantity<T>, t: Double): InterpolatingQuantity<T> {
        if (t < 0.0) return this else if (t >= 1.0) return upper
        val range = upper.value - value
        val y = range * t + value
        return InterpolatingQuantity(y)
    }

    override fun inverseInterpolate(upper: InterpolatingQuantity<T>, query: InterpolatingQuantity<T>): Double {
        val range = upper.value - value
        if (range.value.toDouble() < 0) return 0.0
        val queryRelative: Quantity<T> = query.value - value
        if (queryRelative.value.toDouble() < 0) return 0.0
        return (queryRelative / range).value.toDouble()
    }

    override fun compareTo(other: InterpolatingQuantity<T>): Int {
        return value.value.toDouble().compareTo(other.value.to(value.unit).value.toDouble())
    }
}