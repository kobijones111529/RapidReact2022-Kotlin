package frc.robot.utils

interface Interpolable<T> {
  fun interpolate(upper: T, t: Double): T
}