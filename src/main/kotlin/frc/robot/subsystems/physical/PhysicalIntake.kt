package frc.robot.subsystems.physical

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.DoubleSolenoid.Value
import edu.wpi.first.wpilibj.PneumaticsModuleType
import frc.robot.subsystems.Intake

class PhysicalIntake(private val properties: Properties) : Intake {
    data class Properties(
        val motorID: Int,
        val pneumaticsModuleType: PneumaticsModuleType,
        val extenderForwardChannel: Int,
        val extenderReverseChannel: Int,
        val extenderExtendedValue: Value
    ) {
        val extenderRetractedValue: Value
            get() {
                return when (extenderExtendedValue) {
                    Value.kForward -> Value.kReverse
                    Value.kReverse -> Value.kForward
                    Value.kOff -> Value.kOff
                }
            }
    }

    private val motor = TalonSRX(properties.motorID)
    private val extender = DoubleSolenoid(
        properties.pneumaticsModuleType,
        properties.extenderForwardChannel,
        properties.extenderReverseChannel
    )

    /**
     * Update motor output
     */
    private var motorOutput = {
        motor.set(ControlMode.PercentOutput, 0.0)
    }

    override var extended: Boolean
        get() = extender.get() == properties.extenderExtendedValue
        set(value) {
            extender.set(if (value) properties.extenderExtendedValue else properties.extenderRetractedValue)
        }

    init {
        motor.configFactoryDefault()

        motor.enableVoltageCompensation(true)
    }

    /**
     * Set motor output
     */
    override fun setOutput(output: Double) {
        motorOutput = {
            motor.set(ControlMode.PercentOutput, output)
        }
    }

    override fun periodic() {
        motorOutput()
    }
}