package frc.robot.subsystems

import si.uom.quantity.AngularAcceleration
import si.uom.quantity.AngularSpeed
import javax.measure.Quantity

interface EncoderShooter : SimpleShooter {
  val speed: Quantity<AngularSpeed>
  fun setSpeed(speed: Quantity<AngularSpeed>)
  fun setSpeed(speed: Quantity<AngularSpeed>, accelerationLimit: Quantity<AngularAcceleration>)
}