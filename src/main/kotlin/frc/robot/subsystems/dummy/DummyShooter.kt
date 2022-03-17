package frc.robot.subsystems.dummy

import frc.robot.subsystems.Shooter
import si.uom.quantity.AngularAcceleration
import si.uom.quantity.AngularSpeed
import systems.uom.common.USCustomary
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity

class DummyShooter : Shooter {
  override val speed: Quantity<AngularSpeed>
    get() = Quantities.getQuantity(0, USCustomary.REVOLUTION_PER_MINUTE)

  override fun setOutput(output: Double) {

  }

  override fun setOutput(output: Double, rateLimit: Double) {

  }

  override fun setSpeed(speed: Quantity<AngularSpeed>) {

  }

  override fun setSpeed(speed: Quantity<AngularSpeed>, accelerationLimit: Quantity<AngularAcceleration>) {

  }
}