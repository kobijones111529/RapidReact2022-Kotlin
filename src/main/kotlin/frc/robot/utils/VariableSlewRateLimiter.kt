package frc.robot.utils

import edu.wpi.first.math.MathUtil
import edu.wpi.first.util.WPIUtilJNI

class VariableSlewRateLimiter @JvmOverloads constructor(private var prevVal: Double = 0.0) {
    private var prevTime: Double

    init {
        prevTime = WPIUtilJNI.now() * 1e-6
    }

    fun calculate(input: Double, limit: Double): Double {
        val currentTime = WPIUtilJNI.now() * 1e-6
        val elapsedTime = currentTime - prevTime
        prevVal += MathUtil.clamp(input - prevVal, -limit * elapsedTime, limit * elapsedTime)
        prevTime = currentTime
        return prevVal
    }

    fun reset(value: Double) {
        prevVal = value
        prevTime = WPIUtilJNI.now() * 1e-6
    }
}