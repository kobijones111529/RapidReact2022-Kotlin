package frc.robot.subsystems.dummy

import frc.robot.subsystems.Drivetrain
import si.uom.SI
import si.uom.quantity.AngularAcceleration
import si.uom.quantity.AngularSpeed
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Acceleration
import javax.measure.quantity.Speed

class DummyDrivetrain : Drivetrain {
  override val leftVelocity: Quantity<Speed>
    get() = Quantities.getQuantity(0, SI.METRE_PER_SECOND)
  override val rightVelocity: Quantity<Speed>
    get() = Quantities.getQuantity(0, SI.METRE_PER_SECOND)
  override val velocity: Quantity<Speed>
    get() = Quantities.getQuantity(0, SI.METRE_PER_SECOND)
  override val angularVelocity: Quantity<AngularSpeed>
    get() = Quantities.getQuantity(0, SI.RADIAN_PER_SECOND)
  override var lowGear: Boolean = false

  override fun arcadeDrive(move: Double, turn: Double) {

  }

  override fun arcadeDrive(move: Double, turn: Double, moveRateLimit: Double, turnRateLimit: Double) {

  }

  override fun velocityArcadeDrive(speed: Quantity<Speed>, angularSpeed: Quantity<AngularSpeed>) {

  }

  override fun velocityArcadeDrive(
    xSpeed: Quantity<Speed>,
    zRotation: Quantity<AngularSpeed>,
    accelerationLimit: Quantity<Acceleration>,
    angularAccelerationLimit: Quantity<AngularAcceleration>
  ) {

  }
}