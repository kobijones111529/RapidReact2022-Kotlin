package frc.robot.io

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj2.command.button.Trigger
import frc.robot.Constants

object Inputs {
  enum class TuningMode {
    NONE,
    SHOOTER
  }

  private val networkTable: NetworkTable = NetworkTableInstance.getDefault().getTable("Inputs")
  private val shooterSpeedEntry: NetworkTableEntry = run {
    val entry: NetworkTableEntry = networkTable.getEntry("Shooter speed")
    entry.setNumber(0.0)
    entry
  }

  private val tuningModeChooser: SendableChooser<TuningMode> = run {
    val chooser: SendableChooser<TuningMode> = SendableChooser()
    chooser.setDefaultOption("Not tuning", TuningMode.NONE)
    chooser.addOption("Tuning shooter", TuningMode.SHOOTER)
    chooser
  }
  var tuningMode: TuningMode = tuningModeChooser.selected
    private set

  private val xbox = XboxController(Constants.XBOX_PORT)
  private val extreme = Extreme(Constants.EXTREME_PORT)
  private val buttonBox = ButtonBox(Constants.BUTTON_BOX_PORT)

  val move: Double
    get() = xbox.leftY
  val turn: Double
    get() = xbox.rightX
  val shooterSpeed: Double
    get() = shooterSpeedEntry.getNumber(0.0).toDouble()

  val tuningModeChanged: Trigger = Trigger {
    val previous = tuningMode
    tuningMode = tuningModeChooser.selected
    previous == tuningMode
  }

  val shift: Trigger = Trigger { false }
  val shiftLowGear: Trigger = Trigger { false }
  val shiftHighGear: Trigger = Trigger { false }
  val extendIntake: Trigger = Trigger { false }
  val retractIntake: Trigger = Trigger { false }
  val toggleIntake: Trigger = Trigger { false }
  val runIntakeIn: Trigger = Trigger { false }
  val runIntakeOut: Trigger = Trigger { false }
  val runMagazineIn: Trigger = Trigger { false }
  val runMagazineOut: Trigger = Trigger { false }
  val runShooter: Trigger = Trigger { false }
  val autoAim: Trigger = Trigger { false }
  val autoShootHigh: Trigger = Trigger { false }
}