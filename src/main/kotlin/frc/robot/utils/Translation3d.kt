package frc.robot.utils

class Translation3d : Interpolable<Translation3d> {
    private val v: Vector3

    var x: Double
        get() = v.x
        set(value) {
            v.x = value
        }
    var y: Double
        get() = v.y
        set(value) {
            v.y = value
        }
    var z: Double
        get() = v.z
        set(value) {
            v.z = value
        }
    val norm: Double
        get() = v.magnitude

    constructor(v: Vector3) {
        this.v = v
    }

    operator fun plus(other: Translation3d): Translation3d {
        return Translation3d(v + other.v)
    }

    operator fun minus(other: Translation3d): Translation3d {
        return Translation3d(v - other.v)
    }

    operator fun unaryMinus(): Translation3d {
        return Translation3d(-v)
    }

    operator fun times(scalar: Double): Translation3d {
        return Translation3d(v * scalar)
    }

    operator fun div(scalar: Double): Translation3d {
        return Translation3d(v / scalar)
    }

    fun getDistance(other: Translation3d): Double {
        return (other.v - v).magnitude
    }

    fun rotateBy(other: Rotation3d): Translation3d {
        return Translation3d(v.rotate(other.quaternion))
    }

    override fun interpolate(upper: Translation3d, t: Double): Translation3d {
        return Translation3d(v.interpolate(upper.v, t))
    }
}