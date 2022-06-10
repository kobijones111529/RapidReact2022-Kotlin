package frc.robot.subsystems

import si.uom.quantity.AngularAcceleration
import si.uom.quantity.AngularSpeed
import javax.measure.Quantity
import javax.measure.quantity.Acceleration
import javax.measure.quantity.Speed

interface EncoderDrivetrain : SimpleDrivetrain {
    val leftVelocity: Quantity<Speed>
    val rightVelocity: Quantity<Speed>
    val velocity: Quantity<Speed>
    val angularVelocity: Quantity<AngularSpeed>

    fun velocityArcadeDrive(speed: Quantity<Speed>, angularSpeed: Quantity<AngularSpeed>)

    fun velocityArcadeDrive(
        xSpeed: Quantity<Speed>,
        zRotation: Quantity<AngularSpeed>,
        accelerationLimit: Quantity<Acceleration>,
        angularAccelerationLimit: Quantity<AngularAcceleration>
    )
}