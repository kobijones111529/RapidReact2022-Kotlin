package frc.robot.io

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.button.Trigger
import frc.robot.Constants

object Inputs {
  private val networkTable: NetworkTable = NetworkTableInstance.getDefault().getTable("Inputs")

  private val xbox = XboxController(Constants.XBOX_PORT)
  private val extreme = Extreme(Constants.EXTREME_PORT)
  private val buttonBox = ButtonBox(Constants.BUTTON_BOX_PORT)

  val move: Double
    get() = xbox.leftY
  val turn: Double
    get() = xbox.rightX

  val shift = Trigger { false }
  val shiftLowGear = Trigger { false }
  val shiftHighGear = Trigger { false }
  val extendIntake = Trigger { false }
  val retractIntake = Trigger { false }
  val toggleIntake = Trigger { false }
  val runIntakeIn = Trigger { false }
  val runIntakeOut = Trigger { false }
  val runMagazineIn = Trigger { false }
  val runMagazineOut = Trigger { false }
  val runShooter = Trigger { false }
  val autoAim = Trigger { false }
  val autoShootHigh = Trigger { false }
}