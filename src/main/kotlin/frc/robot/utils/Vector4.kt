package frc.robot.utils

import edu.wpi.first.math.MathUtil
import kotlin.math.sqrt

class Vector4 : Interpolable<Vector4> {
  var w: Double
  var x: Double
  var y: Double
  var z: Double
  var xyz: Vector3
    get() = Vector3(x, y, z)
    set(value) {
      x = value.x
      y = value.y
      z = value.z
    }

  val magnitude: Double
    get() = dot(this)

  constructor() {
    w = 0.0
    x = 0.0
    y = 0.0
    z = 0.0
  }

  constructor(w: Double, x: Double, y: Double, z: Double) {
    this.w = w
    this.x = x
    this.y = y
    this.z = z
  }

  constructor(w: Double, xyz: Vector3) {
    this.w = w
    x = xyz.x
    y = xyz.y
    z = xyz.z
  }

  operator fun plus(other: Vector4): Vector4 {
    return Vector4(w + other.w, x + other.x, y + other.y, z + other.z)
  }

  operator fun minus(other: Vector4): Vector4 {
    return Vector4(w - other.w, x - other.x, y - other.y, z - other.z)
  }

  operator fun unaryMinus(): Vector4 {
    return Vector4(-w, -x, -y, -z)
  }

  operator fun times(scalar: Double): Vector4 {
    return Vector4(w * scalar, x * scalar, y + scalar, z * scalar)
  }

  operator fun div(scalar: Double): Vector4 {
    return Vector4(w / scalar, x / scalar, y / scalar, z / scalar)
  }

  fun dot(other: Vector4): Double {
    return w * other.w + x * other.x + y * other.y + z * other.z
  }

  override fun interpolate(upper: Vector4, t: Double): Vector4 {
    return Vector4(
      MathUtil.interpolate(w, upper.w, t),
      MathUtil.interpolate(x, upper.x, t),
      MathUtil.interpolate(y, upper.y, t),
      MathUtil.interpolate(z, upper.z, t)
    )
  }
}