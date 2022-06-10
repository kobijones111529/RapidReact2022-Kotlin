package frc.robot.subsystems

import edu.wpi.first.wpilibj2.command.Subsystem
import frc.robot.utils.PIDConstants

interface Shooter : EncoderShooter, Subsystem {
    fun updatePID(data: PIDConstants)
}