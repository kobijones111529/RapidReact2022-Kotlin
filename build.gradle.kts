import edu.wpi.first.deployutils.deploy.artifact.FileTreeArtifact
import edu.wpi.first.gradlerio.GradleRIOPlugin
import edu.wpi.first.gradlerio.deploy.roborio.FRCJavaArtifact
import edu.wpi.first.gradlerio.deploy.roborio.RoboRIO
import edu.wpi.first.toolchain.NativePlatforms

plugins {
    id("edu.wpi.first.GradleRIO") version "2022.4.1"
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("edu.wpi.first.wpilib.repositories.WPILibRepositoriesPlugin") version "2020.2"
}

val robotMainClass: String = "frc.robot.Main"

// Define my targets (RoboRIO) and artifacts (deployable files)
// This is added by GradleRIO's backing project DeployUtils.
deploy {
    targets.create("roborio", RoboRIO::class) {
        // Team number is loaded either from the .wpilib/wpilib_preferences.json
        // or from command line. If not found an exception will be thrown.
        // You can use getTeamOrDefault(team) instead of getTeamNumber if you
        // want to store a team number in this file.
        team = project.frc.teamNumber
        debug.set(project.frc.getDebugOrDefault(false))

        artifacts.create("frcJava", FRCJavaArtifact::class) { }
        artifacts.create("frcStaticFileDeploy", FileTreeArtifact::class) {
            files.set(project.fileTree("src/main/deploy"))
            directory.set("/home/lvuser/deploy")
        }
    }
}

val deployArtifact: FRCJavaArtifact = deploy.targets["roborio"].artifacts["frcJava"] as FRCJavaArtifact

// Set to true to use debug for JNI.
wpi.java.debugJni.set(false)

// Set this to true to enable desktop support.
val includeDesktopSupport: Boolean = false

repositories {
    mavenCentral()
}

// Defining my dependencies. In this case, WPILib (+ friends), and vendor libraries.
// Also defines JUnit 4.
dependencies {
    implementation("javax.measure:unit-api:2.1.3")
    implementation("tech.units:indriya:2.1.3")
    implementation("systems.uom:systems-common:2.1")

    wpi.java.deps.wpilib().forEach { implementation(it) }
    wpi.java.vendor.java().forEach { implementation(it) }

    wpi.java.deps.wpilibJniDebug(NativePlatforms.roborio).forEach { add("roborioDebug", it) }
    wpi.java.vendor.jniDebug(NativePlatforms.roborio).forEach { add("roborioDebug", it) }

    wpi.java.deps.wpilibJniRelease(NativePlatforms.roborio).forEach { add("roborioRelease", it) }
    wpi.java.vendor.jniRelease(NativePlatforms.roborio).forEach { add("roborioRelease", it) }

    wpi.java.deps.wpilibJniDebug(NativePlatforms.desktop).forEach { add("nativeDebug", it) }
    wpi.java.vendor.jniDebug(NativePlatforms.desktop).forEach { add("nativeDebug", it) }
    wpi.sim.enableDebug().forEach { simulationDebug(it) }

    wpi.java.deps.wpilibJniRelease(NativePlatforms.desktop).forEach { add("nativeRelease", it) }
    wpi.java.vendor.jniRelease(NativePlatforms.desktop).forEach { add("nativeRelease", it) }
    wpi.sim.enableRelease().forEach { simulationRelease(it) }

    testImplementation("junit:junit:4.13.2")
}

// Simulation configuration (e.g. environment variables).
wpi.sim.addGui().defaultEnabled.set(true)
wpi.sim.addDriverstation()

// Setting up my Jar File. In this case, adding all libraries into the main jar ('fat jar')
// in order to make them all available at runtime. Also adding the manifest so WPILib
// knows where to look for our Robot Class.
tasks.jar {
    from({ configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) } })
    manifest { GradleRIOPlugin.javaManifest(robotMainClass) }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

// Configure jar and deploy tasks
deployArtifact.setJarTask(tasks.jar)
wpi.java.configureExecutableTasks(tasks.jar.get())
wpi.java.configureTestTasks(tasks.test.get())
