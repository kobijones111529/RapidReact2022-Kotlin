package frc.robot.subsystems

import edu.wpi.first.wpilibj2.command.Subsystem

interface Magazine : Subsystem {
  fun setOutput(output: Double)
}