package frc.robot.subsystems

import edu.wpi.first.wpilibj2.command.Subsystem

interface Intake : Subsystem {
  val extended: Boolean

  fun setOutput(output: Double)
}