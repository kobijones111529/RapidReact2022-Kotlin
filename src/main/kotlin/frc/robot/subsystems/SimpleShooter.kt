package frc.robot.subsystems

interface SimpleShooter {
    fun setOutput(output: Double)
    fun setOutput(output: Double, rateLimit: Double)
}