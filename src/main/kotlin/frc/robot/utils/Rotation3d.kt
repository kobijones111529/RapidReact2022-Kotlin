package frc.robot.utils

import edu.wpi.first.math.MathUtil

class Rotation3d : Interpolable<Rotation3d> {
    val quaternion: Quaternion

    constructor() {
        quaternion = Quaternion(0.0, Vector3(1.0, 0.0, 0.0))
    }

    private constructor(quaternion: Quaternion) {
        this.quaternion = quaternion
    }

    operator fun plus(other: Rotation3d): Rotation3d {
        return rotateBy(other)
    }

    operator fun minus(other: Rotation3d): Rotation3d {
        return rotateBy(-other)
    }

    operator fun unaryMinus(): Rotation3d {
        return Rotation3d(quaternion.inverse)
    }

    fun rotateBy(other: Rotation3d): Rotation3d {
        return Rotation3d(quaternion * other.quaternion)
    }

    override fun interpolate(upper: Rotation3d, t: Double): Rotation3d {
        val tClamped = MathUtil.clamp(t, 0.0, 1.0)
        TODO()
    }
}