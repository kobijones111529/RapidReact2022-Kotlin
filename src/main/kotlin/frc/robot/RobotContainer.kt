package frc.robot

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.commands.*
import frc.robot.io.Inputs
import frc.robot.io.Limelight
import frc.robot.io.PhysicalLimelight
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Magazine
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.dummy.DummyDrivetrain
import frc.robot.subsystems.dummy.DummyIntake
import frc.robot.subsystems.dummy.DummyMagazine
import frc.robot.subsystems.dummy.DummyShooter
import frc.robot.subsystems.physical.PhysicalDrivetrain
import frc.robot.subsystems.physical.PhysicalIntake
import frc.robot.subsystems.physical.PhysicalMagazine
import frc.robot.subsystems.physical.PhysicalShooter
import frc.robot.utils.CameraUtils
import frc.robot.utils.InterpolatingQuantity

class RobotContainer {
    private val networkTable = NetworkTableInstance.getDefault().getTable(javaClass.simpleName)

    private val limelight: Limelight = PhysicalLimelight(Constants.LIMELIGHT_TABLE_NAME)

    private var drivetrain: Drivetrain =
        if (Constants.DRIVETRAIN_ENABLED) PhysicalDrivetrain(Constants.DRIVETRAIN_PROPERTIES) else DummyDrivetrain()
    private var intake: Intake =
        if (Constants.INTAKE_ENABLED) PhysicalIntake(Constants.INTAKE_PROPERTIES) else DummyIntake()
    private var magazine: Magazine =
        if (Constants.MAGAZINE_ENABLED) PhysicalMagazine(Constants.MAGAZINE_PROPERTIES) else DummyMagazine()
    private var shooter: Shooter =
        if (Constants.SHOOTER_ENABLED) PhysicalShooter(Constants.SHOOTER_PROPERTIES) else DummyShooter()

    val autonomousCommand: Command? = null

    init {
        drivetrain.defaultCommand = arcadeDrive(drivetrain, Inputs::move, Inputs::turn)
        shooter.defaultCommand = if (Inputs.tuningMode == Inputs.TuningMode.SHOOTER) null else null

        Inputs.tuningModeChanged.whenActive(
            setShooterTuningMode(
                shooter,
                Inputs.tuningMode == Inputs.TuningMode.SHOOTER
            )
        )

        Inputs.shiftLowGear.whenActive(shift(drivetrain, true))
        Inputs.shiftHighGear.whenActive(shift(drivetrain, false))
        Inputs.shift.whenActive(shift(drivetrain))
        Inputs.extendIntake.whenActive(setIntakeExtended(intake, true))
        Inputs.retractIntake.whenActive(setIntakeExtended(intake, false))
        Inputs.toggleIntake.whenActive(toggleIntakeExtended(intake))
        Inputs.runIntakeIn.whileActiveContinuous(runIntake(intake) { Constants.INTAKE_RUN_SPEED })
        Inputs.runIntakeOut.whileActiveContinuous(runIntake(intake) { -Constants.INTAKE_RUN_SPEED })
        Inputs.runMagazineIn.whileActiveContinuous(runMagazine(magazine) { Constants.MAGAZINE_RUN_SPEED })
        Inputs.runMagazineOut.whileActiveContinuous(runMagazine(magazine) { -Constants.MAGAZINE_RUN_SPEED })
        Inputs.runShooter.whileActiveContinuous(runShooter(shooter) { Constants.SHOOTER_RUN_SPEED })
        Inputs.autoAim.whileActiveContinuous(
            autoAim(
                drivetrain,
                { limelight.targetOffset?.x },
                Constants.TARGET_OFFSET_TOLERANCE
            )
        )
        Inputs.autoShootHigh.whileActiveContinuous(
            aimAndShoot(
                drivetrain,
                magazine,
                shooter,
                { limelight.targetOffset?.x },
                Constants.TARGET_OFFSET_TOLERANCE,
                { Constants.MAGAZINE_RUN_SPEED },
                {
                    CameraUtils.distance(
                        Constants.LIMELIGHT_MOUNT_ANGLE,
                        Constants.VISION_TARGET_HEIGHT,
                        limelight.targetOffset?.y
                    )?.let {
                        InterpolatingQuantity(it)
                    }?.let {
                        Constants.SHOOTER_MAP[it]?.value
                    }
                },
                Constants.SHOOTER_SPEED_TOLERANCE,
                Constants.MAGAZINE_BACK_RUN_TIME
            )
        )
    }
}