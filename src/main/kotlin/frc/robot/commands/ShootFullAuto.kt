package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.EncoderShooter
import frc.robot.subsystems.Magazine
import frc.robot.subsystems.Shooter
import java.util.function.Supplier
import kotlin.math.abs

class ShootFullAuto(
  private val magazine: Magazine,
  private val shooter: Shooter,
  private val distanceSupplier: Supplier<Double?>,
  private val angleOffsetSupplier: Supplier<Double?>,
  private val shooterDistanceToRPM: (distance: Double) -> Double
) : CommandBase() {
  init {
    addRequirements(magazine, shooter)
  }

  override fun initialize() {

  }

  override fun execute() {
    val distance = distanceSupplier.get()
    val angleOffset = angleOffsetSupplier.get()

    if (distance != null && angleOffset != null) {

    }
  }
}