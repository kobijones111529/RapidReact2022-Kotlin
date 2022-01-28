package frc.robot

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler

class Robot : TimedRobot() {
    private lateinit var robotContainer: RobotContainer

    override fun robotInit() {
        robotContainer = RobotContainer()
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
    }

    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
    }
}