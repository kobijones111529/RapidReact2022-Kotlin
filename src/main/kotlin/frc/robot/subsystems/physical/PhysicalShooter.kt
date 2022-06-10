package frc.robot.subsystems.physical

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import frc.robot.subsystems.Shooter
import frc.robot.utils.CustomUnits
import frc.robot.utils.SimpleMotorFeedforwardConstants
import frc.robot.utils.PIDConstants
import frc.robot.utils.VariableSlewRateLimiter
import si.uom.quantity.AngularAcceleration
import si.uom.quantity.AngularSpeed
import systems.uom.common.USCustomary
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity

class PhysicalShooter(private val properties: Properties) : Shooter {
    data class Properties(
        val motorID: Int,
        val encoderResolution: Int,
        val encoderToOutputRatio: Double,
        var motorFeedforwardConstants: SimpleMotorFeedforwardConstants,
        var motorVelocityPIDConstants: PIDConstants
    )

    var feedforwardConstants: SimpleMotorFeedforwardConstants
        get() = properties.motorFeedforwardConstants
        set(value) {
            if (value != properties.motorFeedforwardConstants) {
                motorFeedforward = SimpleMotorFeedforward(value.s, value.v, value.a)
                properties.motorFeedforwardConstants = value
            }
        }
    var pidConstants: PIDConstants
        get() = properties.motorVelocityPIDConstants
        set(value) {
            if (value != properties.motorVelocityPIDConstants) {
                motor.config_kP(0, value.p)
                motor.config_kI(0, value.i)
                motor.config_kD(0, value.d)
                properties.motorVelocityPIDConstants = value
            }
        }

    private val motor = WPI_TalonSRX(properties.motorID)

    private var motorFeedforward = SimpleMotorFeedforward(
        properties.motorFeedforwardConstants.s,
        properties.motorFeedforwardConstants.v,
        properties.motorFeedforwardConstants.a
    )

    private val rateLimiter = VariableSlewRateLimiter()
    private val accelerationLimiter = VariableSlewRateLimiter()

    private var motorOutput = {
        motor.set(ControlMode.PercentOutput, 0.0)
    }

    init {
        motor.configFactoryDefault()

        // The toRPM here is counterintuitive, but the PID gain units are technically inverse rpm,
        // so toRPM converts them to inverse sensorUnitsPer100Milliseconds
        motor.config_kP(0, properties.motorVelocityPIDConstants.p)
        motor.config_kI(0, properties.motorVelocityPIDConstants.i)
        motor.config_kD(0, properties.motorVelocityPIDConstants.d)

        // Consistent motor output for varying battery voltages
        motor.enableVoltageCompensation(true)
    }

    override val speed: Quantity<AngularSpeed>
        get() {
            val speedSensorUnitsPer100Milliseconds = motor.selectedSensorPosition
            val speed = Quantities.getQuantity(
                speedSensorUnitsPer100Milliseconds / properties.encoderResolution * properties.encoderToOutputRatio,
                CustomUnits.REVOLUTION_PER_100_MILLISECOND
            )
            return speed.toSystemUnit()
        }

    override fun setOutput(output: Double) {
        motorOutput = {
            motor.set(output)
        }
    }

    override fun setOutput(output: Double, rateLimit: Double) {
        val newOutput = rateLimiter.calculate(output, rateLimit)
        setOutput(newOutput)
    }

    override fun setSpeed(speed: Quantity<AngularSpeed>) {
        val feedforward = motorFeedforward.calculate(speed.to(USCustomary.REVOLUTION_PER_MINUTE).value.toDouble())

        val sensorUnitsPer100Milliseconds =
            speed.to(CustomUnits.REVOLUTION_PER_100_MILLISECOND).value.toDouble() / properties.encoderToOutputRatio * properties.encoderResolution

        motorOutput = {
            motor.set(ControlMode.Velocity, sensorUnitsPer100Milliseconds, DemandType.ArbitraryFeedForward, feedforward)
        }
    }

    override fun setSpeed(speed: Quantity<AngularSpeed>, accelerationLimit: Quantity<AngularAcceleration>) {
        val newSpeed = accelerationLimiter.calculate(
            speed.to(USCustomary.REVOLUTION_PER_MINUTE).value.toDouble() / properties.encoderToOutputRatio,
            accelerationLimit.to(CustomUnits.REVOLUTION_PER_MINUTE_PER_SECOND).value.toDouble()
        )
        setSpeed(Quantities.getQuantity(newSpeed, USCustomary.REVOLUTION_PER_MINUTE))
    }

    override fun periodic() {
        motorOutput()
    }

    override fun updatePID(data: PIDConstants) {
        motor.config_kP(0, data.p)
        motor.config_kI(0, data.i)
        motor.config_kD(0, data.d)
    }
}