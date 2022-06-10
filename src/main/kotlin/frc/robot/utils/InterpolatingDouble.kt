package frc.robot.utils

class InterpolatingDouble(val value: Double) : Interpolable<InterpolatingDouble>,
    InverseInterpolable<InterpolatingDouble>, Comparable<InterpolatingDouble> {
    override fun interpolate(upper: InterpolatingDouble, t: Double): InterpolatingDouble {
        val range = upper.value - value
        val y = t * range + value
        return InterpolatingDouble(y)
    }

    override fun inverseInterpolate(upper: InterpolatingDouble, query: InterpolatingDouble): Double {
        val range = upper.value - value
        if (range < 0) return 0.0
        val queryRelative = query.value - value
        if (queryRelative < 0) return 0.0
        return queryRelative / range
    }

    override fun compareTo(other: InterpolatingDouble): Int {
        return value.compareTo(other.value)
    }
}