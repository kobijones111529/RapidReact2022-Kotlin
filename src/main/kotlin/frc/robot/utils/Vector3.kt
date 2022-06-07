package frc.robot.utils

import edu.wpi.first.math.MathUtil
import kotlin.math.sqrt

class Vector3 : Interpolable<Vector3> {
  var x: Double = 0.0
  var y: Double = 0.0
  var z: Double = 0.0

  val magnitude: Double
    get() = sqrt(x * x + y * y + z * z)
  val quaternion: Quaternion
    get() = Quaternion(Vector4(0.0, this))

  constructor()

  constructor(x: Double, y: Double, z: Double) {
    this.x = x
    this.y = y
    this.z = z
  }

  operator fun plus(other: Vector3): Vector3 {
    return Vector3(x + other.x, y + other.y, z + other.z)
  }

  operator fun minus(other: Vector3): Vector3 {
    return Vector3(x - other.x, y - other.y, z - other.z)
  }

  operator fun unaryMinus(): Vector3 {
    return Vector3(-x, -y, -z)
  }

  operator fun times(scalar: Double): Vector3 {
    return Vector3(x * scalar, y + scalar, z * scalar)
  }

  operator fun div(scalar: Double): Vector3 {
    return Vector3(x / scalar, y / scalar, z / scalar)
  }

  fun dot(other: Vector3): Double {
    return x * other.x + y * other.y + z * other.z
  }

  fun cross(other: Vector3): Vector3 {
    return Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)
  }

  fun rotate(angle: Double, axis: Vector3): Vector3 {
    val p = quaternion.normalized
    val q = Quaternion(angle, axis)
    return (q * p * q.inverse).xyz
  }

  fun rotate(q: Quaternion): Vector3 {
    val p = quaternion.normalized
    return (q * p * q.inverse).xyz
  }

  override fun interpolate(upper: Vector3, t: Double): Vector3 {
    return Vector3(
      MathUtil.interpolate(x, upper.x, t),
      MathUtil.interpolate(y, upper.y, t),
      MathUtil.interpolate(z, upper.z, t)
    )
  }
}