package frc.robot.utils

import si.uom.SI
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Angle
import kotlin.math.abs
import kotlin.math.tan

operator fun <Q : Quantity<Q>> Quantity<Q>.plus(addend: Quantity<Q>): Quantity<Q> {
  return add(addend)
}

operator fun <Q : Quantity<Q>> Quantity<Q>.minus(subtrahend: Quantity<Q>): Quantity<Q> {
  return subtract(subtrahend)
}

operator fun <Q : Quantity<Q>> Quantity<Q>.times(multiplicand: Number): Quantity<Q> {
  return multiply(multiplicand)
}

operator fun <Q : Quantity<Q>> Quantity<Q>.times(multiplicand: Quantity<*>): Quantity<*> {
  return multiply(multiplicand)
}

operator fun <Q : Quantity<Q>> Quantity<Q>.div(divisor: Number): Quantity<Q> {
  return divide(divisor)
}

operator fun <Q : Quantity<Q>> Quantity<Q>.div(divisor: Quantity<*>): Quantity<*> {
  return divide(divisor)
}

operator fun <Q : Quantity<Q>> Quantity<Q>.compareTo(other: Quantity<Q>): Int {
  return value.toDouble().compareTo(other.to(unit).value.toDouble())
}

fun <Q : Quantity<Q>> diff(a: Quantity<Q>, b: Quantity<Q>): Quantity<Q> {
  val d = b - a
  val u = d.unit
  return Quantities.getQuantity(abs(d.value.toDouble()), u)
}

fun tan(a: Quantity<Angle>): Double {
  return tan(a.to(SI.RADIAN).value.toDouble())
}