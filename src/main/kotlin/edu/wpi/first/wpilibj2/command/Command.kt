package edu.wpi.first.wpilibj2.command

fun Command.onlyWhen(condition: () -> Boolean): Command {
    return WaitUntilCommand(condition).andThen(this).withInterrupt { !condition() }
}