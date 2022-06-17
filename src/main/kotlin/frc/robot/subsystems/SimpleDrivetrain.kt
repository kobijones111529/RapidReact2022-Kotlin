package frc.robot.subsystems

interface SimpleDrivetrain {
    fun neutralOutput()
    fun arcadeDrive(move: Double, turn: Double)
    fun arcadeDrive(move: Double, turn: Double, moveRateLimit: Double, turnRateLimit: Double)
}