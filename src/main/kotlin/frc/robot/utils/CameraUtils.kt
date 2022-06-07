package frc.robot.utils

import javax.measure.Quantity
import javax.measure.quantity.Angle
import javax.measure.quantity.Length

object CameraUtils {
  fun distance(
    cameraAngle: Quantity<Angle>,
    targetHeight: Quantity<Length>,
    targetOffset: Quantity<Angle>?
  ): Quantity<Length>? {
    return targetOffset?.let {
      return targetHeight / tan(cameraAngle + it)
    }
  }
}