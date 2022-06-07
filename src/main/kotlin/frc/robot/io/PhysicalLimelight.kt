package frc.robot.io

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import systems.uom.common.USCustomary
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.quantity.Angle

class PhysicalLimelight(networkTableName: String) : Limelight {
  private val networkTable: NetworkTable = NetworkTableInstance.getDefault().getTable(networkTableName)
  private val hasTargetEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.hasTarget)
  private val targetOffsetXEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.targetOffsetX)
  private val targetOffsetYEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.targetOffsetY)
  private val targetAreaEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.targetArea)
  private val skewEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.skew)
  private val getPipelineEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.getPipeline)
  private val ledModeEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.ledMode)
  private val camModeEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.camMode)
  private val setPipelineEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.setPipeline)
  private val pythonInEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.pythonIn)
  private val pythonOutEntry: NetworkTableEntry = networkTable.getEntry(LimelightAPIKeys.pythonOut)

  private val targetOffsetX: Quantity<Angle>?
    get() = if (hasTarget) Quantities.getQuantity(targetOffsetXEntry.getDouble(0.0), USCustomary.DEGREE_ANGLE) else null
  private val targetOffsetY: Quantity<Angle>?
    get() = if (hasTarget) Quantities.getQuantity(targetOffsetYEntry.getDouble(0.0), USCustomary.DEGREE_ANGLE) else null
  private val fittedBoundingBoxShort: Quantity<Angle>?
    get() = TODO("Math")
  private val fittedBoundingBoxLong: Quantity<Angle>?
    get() = TODO("Math")
  private val roughBoundingBoxHorizontal: Quantity<Angle>?
    get() = TODO("Math")
  private val roughBoundingBoxVertical: Quantity<Angle>?
    get() = TODO("Math")

  // Data
  override val hasTarget: Boolean
    get() = hasTargetEntry.getBoolean(false)
  override val targetOffset: Point<Quantity<Angle>>?
    get() {
      if (!hasTarget) return null
      val x = targetOffsetX ?: return null
      val y = targetOffsetY ?: return null
      return Point(x, y)
    }
  override val targetArea: Double?
    get() = if (hasTarget) targetAreaEntry.getDouble(0.0) else null
  override val skew: Quantity<Angle>?
    get() = if (hasTarget) Quantities.getQuantity(skewEntry.getDouble(0.0), USCustomary.DEGREE_ANGLE) else null

  // Camera controls
  override var ledMode: Limelight.LEDMode
    get() = Limelight.LEDMode[ledModeEntry.getNumber(0).toInt()] ?: Limelight.LEDMode.AUTO
    set(value) {
      ledModeEntry.setNumber(value.id)
    }
  override var camMode: Limelight.CamMode
    get() = Limelight.CamMode[camModeEntry.getNumber(0).toInt()] ?: Limelight.CamMode.VISION
    set(value) {
      camModeEntry.setNumber(value.id)
    }
  override var pipeline: Int
    get() = getPipelineEntry.getNumber(0).toInt()
    set(value) {
      setPipelineEntry.setNumber(value)
    }

  // Python
  override val pythonIn: Array<Number>
    get() = pythonInEntry.getNumberArray(emptyArray())
  override var pythonOut: Array<Number> = emptyArray()
    set(value) {
      pythonOutEntry.setNumberArray(value)
      field = value
    }
}