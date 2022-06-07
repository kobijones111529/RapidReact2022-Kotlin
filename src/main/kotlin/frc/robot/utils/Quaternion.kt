package frc.robot.utils

import kotlin.math.*

class Quaternion {
  private val v: Vector4

  val w: Double
    get() = v.w
  val xyz: Vector3
    get() = v.xyz
  val magnitude: Double
    get() = sqrt(v.dot(v))
  val norm: Double
    get() {
      val magnitude = magnitude
      return magnitude * magnitude
    }
  val normalized: Quaternion
    get() {
      val norm = norm
      return if (norm == 0.0) this else Quaternion(Vector4(v.w / norm, v.xyz / norm))
    }
  val conjugate: Quaternion
    get() = Quaternion(Vector4(w, -xyz))
  val inverse: Quaternion
    get() = Quaternion(conjugate.v / norm)
  val angleAxis: AngleAxis
    get() {
      val angle = 2 * acos(v.w)
      return if (angle == 0.0 || v.w == 1.0)
        AngleAxis(0.0, Vector3(1.0, 0.0, 0.0))
      else
        AngleAxis(angle, v.xyz / sin(angle / 2.0))
    }

  constructor(v: Vector4) {
    this.v = v
  }

  constructor(angle: Double, axis: Vector3) {
    val cos = cos(angle / 2.0)
    val sin = sin(angle / 2.0)
    v = Vector4(cos, axis * sin)
  }

  operator fun plus(other: Quaternion): Quaternion {
    return Quaternion(v + other.v)
  }

  operator fun minus(other: Quaternion): Quaternion {
    return Quaternion(v - other.v)
  }

  operator fun times(other: Quaternion): Quaternion {
    val v = Vector4(
      v.w * other.v.w - v.xyz.dot(other.v.xyz),
      other.v.xyz * v.w + v.xyz * other.v.w + v.xyz.cross(other.v.xyz)
    )
    return Quaternion(v)
  }

  operator fun times(scalar: Double): Quaternion {
    return Quaternion(v * scalar)
  }

  companion object {

  }
}