package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import edu.wpi.first.wpilibj2.command.SubsystemBase

class Drivetrain() : SubsystemBase() {
    val driveFrontLeft: WPI_TalonSRX = WPI_TalonSRX(0)

    init {

    }
}