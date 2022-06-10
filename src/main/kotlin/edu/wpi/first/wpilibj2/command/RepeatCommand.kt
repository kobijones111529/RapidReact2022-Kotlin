package edu.wpi.first.wpilibj2.command

import edu.wpi.first.wpilibj2.command.CommandGroupBase.registerGroupedCommands
import edu.wpi.first.wpilibj2.command.CommandGroupBase.requireUngrouped

fun Command.repeatedly(): RepeatCommand {
    return RepeatCommand(this)
}

open class RepeatCommand(protected val command: Command) : CommandBase() {
    init {
        requireUngrouped(command)
        registerGroupedCommands(command)
        m_requirements.addAll(command.requirements)
    }

    override fun initialize() {
        command.initialize()
    }

    override fun execute() {
        if (command.isFinished) {
            command.end(false)
            command.initialize()
        } else {
            command.execute()
        }
    }

    override fun end(interrupted: Boolean) {
        command.end(interrupted)
    }

    override fun runsWhenDisabled(): Boolean {
        return command.runsWhenDisabled()
    }
}