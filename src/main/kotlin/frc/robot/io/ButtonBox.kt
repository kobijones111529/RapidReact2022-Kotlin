package frc.robot.io

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj2.command.button.Button
import edu.wpi.first.wpilibj2.command.button.JoystickButton

class ButtonBox(port: Int) {
  private val joystick = Joystick(port)

  val topWhite: Button = JoystickButton(joystick, 2)
  val topRed: Button = JoystickButton(joystick, 6)
  val middleWhite: Button = JoystickButton(joystick, 8)
  val middleRed: Button = JoystickButton(joystick, 4)
  val bottomWhite: Button = JoystickButton(joystick, 5)
  val bottomRed: Button = JoystickButton(joystick, 16)
  val green: Button = JoystickButton(joystick, 7)
  val yellow: Button = JoystickButton(joystick, 15)
  val bigWhite: Button = JoystickButton(joystick, 3)
  val bigRed: Button = JoystickButton(joystick, 14)
}