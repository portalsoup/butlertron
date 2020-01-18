package com.portalsoup.mrbutlertron.commands.messagereceived.usercommands

import com.portalsoup.mrbutlertron.commands.RegisteredCommand
import com.portalsoup.mrbutlertron.commands.messagereceived.GuildMessageReceivedCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.lang.StringBuilder

class CommandList : GuildMessageReceivedCommand("Command List") {

    override fun runPredicate(event: GuildMessageReceivedEvent): Boolean {
        val sanitized = event.message.contentRaw.trim().toLowerCase()
        val matches = sanitized.contains("help") &&
                sanitized.contains("mr") &&
                sanitized.contains("butlertron")
        return matches
    }

    fun getCommandList(): String {
        try {
            val cmdList = StringBuilder("These are my commands:\n\n")
            RegisteredCommand.values()
                .map { formatCommand(it) }
                .forEach { cmdList.append(it)}

            return cmdList.toString()
        } catch (e: RuntimeException) {
            println("Failed: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    override fun run(event: GuildMessageReceivedEvent) {
        event.channel.sendMessage(getCommandList()).queue()
    }

    private fun formatCommand(command: RegisteredCommand): String {
        println(command)
        return "${command.commandName} - ${command.description}${if (command.additionalDetails != null) "\n -- ${command.additionalDetails}" else ""}\n\n"
    }

}