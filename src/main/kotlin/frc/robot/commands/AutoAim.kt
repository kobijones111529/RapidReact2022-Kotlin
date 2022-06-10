package frc.robot.commands

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.Drivetrain
import si.uom.SI
import javax.measure.Quantity
import javax.measure.quantity.Angle
import kotlin.math.abs

fun autoAim(drivetrain: Drivetrain, error: () -> Quantity<Angle>?, threshold: Quantity<Angle>? = null): Command {
    return AutoAim(drivetrain, error, threshold)
}

private class AutoAim(
    private val drivetrain: Drivetrain,
    private val error: () -> Quantity<Angle>?,
    private val threshold: Quantity<Angle>? = null
) : CommandBase() {
    private val pid: PIDController = PIDController(0.1, 0.0, 0.0)

    init {
        addRequirements(drivetrain)
    }

    override fun initialize() {
        pid.setpoint = 0.0
    }

    override fun execute() {
        drivetrain.arcadeDrive(0.0, pid.calculate(error.invoke()?.to(SI.RADIAN)?.value?.toDouble() ?: 0.0))
    }

    override fun end(interrupted: Boolean) {
        drivetrain.arcadeDrive(0.0, 0.0)
    }

    override fun isFinished(): Boolean {
        // Error magnitude less than threshold
        threshold?.let {
            return abs(
                error.invoke()?.to(SI.RADIAN)?.value?.toDouble() ?: return false
            ) < abs(it.to(SI.RADIAN).value.toDouble())
        } ?: return false
    }
}