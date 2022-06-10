package frc.robot.subsystems

import edu.wpi.first.wpilibj2.command.Subsystem

interface Drivetrain : EncoderDrivetrain, Subsystem {
    var lowGear: Boolean
}