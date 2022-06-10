package frc.robot.subsystems.physical

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import frc.robot.subsystems.Magazine

class PhysicalMagazine(properties: Properties) : Magazine {
    data class Properties(val motorID: Int)

    private val motor = TalonSRX(properties.motorID)

    private var motorOutput = {
        motor.set(ControlMode.PercentOutput, 0.0)
    }

    init {
        motor.configFactoryDefault()
    }

    override fun setOutput(output: Double) {
        motorOutput = {
            motor.set(ControlMode.PercentOutput, output)
        }
    }

    override fun periodic() {
        motorOutput()
    }
}