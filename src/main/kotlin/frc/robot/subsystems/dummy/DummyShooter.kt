package frc.robot.subsystems.dummy

import frc.robot.subsystems.Shooter
import frc.robot.utils.PIDConstants
import si.uom.quantity.AngularAcceleration
import si.uom.quantity.AngularSpeed
import systems.uom.common.USCustomary
import tech.units.indriya.quantity.Quantities
import java.util.logging.Logger
import javax.measure.Quantity

class DummyShooter : Shooter {
  private val logger: Logger = Logger.getLogger(this.javaClass.name)

  override val speed: Quantity<AngularSpeed>
    get() = Quantities.getQuantity(0, USCustomary.REVOLUTION_PER_MINUTE)

  private var motorOutput = {
    val output = 0.0
    logger.finest("Shooter output: $output")
  }

  override fun setOutput(output: Double) {
    motorOutput = {
      logger.finest("Shooter output: $output")
    }
  }

  override fun setOutput(output: Double, rateLimit: Double) {
    motorOutput = {
      logger.finest("Shooter output: $output")
    }
  }

  override fun setSpeed(speed: Quantity<AngularSpeed>) {
    motorOutput = {
      logger.finest("Shooter speed: ${speed.value.toDouble()} ${speed.unit.symbol}")
    }
  }

  override fun setSpeed(speed: Quantity<AngularSpeed>, accelerationLimit: Quantity<AngularAcceleration>) {
    motorOutput = {
      logger.finest("Shooter speed: ${speed.value.toDouble()} ${speed.unit.symbol}")
    }
  }

  override fun periodic() {
    motorOutput()
  }

  override fun updatePID(data: PIDConstants) {}
}