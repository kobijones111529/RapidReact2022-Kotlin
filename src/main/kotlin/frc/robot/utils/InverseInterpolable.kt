package frc.robot.utils

interface InverseInterpolable<T> {
  fun inverseInterpolate(upper: T, query: T): Double
}