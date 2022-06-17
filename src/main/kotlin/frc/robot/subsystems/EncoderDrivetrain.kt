package frc.robot.subsystems

import si.uom.quantity.AngularAcceleration
import si.uom.quantity.AngularSpeed
import javax.measure.Quantity
import javax.measure.quantity.Acceleration
import javax.measure.quantity.Speed

interface EncoderDrivetrain : SimpleDrivetrain {
    val leftSpeed: Quantity<Speed>
    val rightSpeed: Quantity<Speed>
    val speed: Quantity<Speed>
    val angularSpeed: Quantity<AngularSpeed>

    fun velocityArcadeDrive(speed: Quantity<Speed>, angularSpeed: Quantity<AngularSpeed>)

    fun velocityArcadeDrive(
        speed: Quantity<Speed>,
        angularSpeed: Quantity<AngularSpeed>,
        accelerationLimit: Quantity<Acceleration>,
        angularAccelerationLimit: Quantity<AngularAcceleration>
    )
}