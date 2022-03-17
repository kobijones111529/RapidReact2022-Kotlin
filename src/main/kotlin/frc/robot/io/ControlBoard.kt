package frc.robot.io

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController

class ControlBoard {
    val xboxController = XboxController(0)
    val joystick = Joystick(1)
    val buttonBox = ButtonBox(2)
}