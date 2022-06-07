package frc.robot.io

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import systems.uom.common.USCustomary
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Angle

class Extreme(port: Int) {
  companion object {
    const val stickXAxis: Int = 0
    const val stickYAxis: Int = 1
    const val stickZAxis: Int = 2
    const val sliderAxis: Int = 3
  }

  private val joystick = Joystick(port)

  val stickX: Double
    get() = joystick.getRawAxis(stickXAxis)
  val stickY: Double
    get() = joystick.getRawAxis(stickYAxis)
  val twist: Double
    get() = joystick.getRawAxis(stickZAxis)
  val slider: Double
    get() = joystick.getRawAxis(sliderAxis)
  val pov: Quantity<Angle>?
    get() = if (joystick.pov == -1) null else Quantities.getQuantity(joystick.pov, USCustomary.DEGREE_ANGLE)

  val trigger = JoystickButton(joystick, 1)
  val side = JoystickButton(joystick, 2)
  val joystickBottomLeft = JoystickButton(joystick, 3)
  val joystickBottomRight = JoystickButton(joystick, 4)
  val joystickTopLeft = JoystickButton(joystick, 5)
  val joystickTopRight = JoystickButton(joystick, 6)
  val baseNearLeft = JoystickButton(joystick, 7)
  val baseNearRight = JoystickButton(joystick, 8)
  val baseMiddleLeft = JoystickButton(joystick, 9)
  val baseMiddleRight = JoystickButton(joystick, 10)
  val baseFarLeft = JoystickButton(joystick, 11)
  val baseFarRight = JoystickButton(joystick, 12)
}