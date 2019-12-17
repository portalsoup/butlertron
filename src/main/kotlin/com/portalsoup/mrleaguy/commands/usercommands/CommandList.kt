package com.portalsoup.mrleaguy.commands.usercommands

import com.portalsoup.mrleaguy.commands.AbstractCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.reflections.Reflections

class CommandList : AbstractCommand() {

    override fun syntaxDescription(): String = "\"help mr butlertron\" or \"help mr. butlertron\""

    override fun runPredicate(event: GuildMessageReceivedEvent): Boolean {
        val sanitized = event.message.contentRaw.trim().toLowerCase()
        return sanitized.equals("help mr butlertron") || sanitized.equals("help mr. butlertron")
    }

    fun getCommandList(): String {
        var cmdList = ""
        val reflections = Reflections().getSubTypesOf(AbstractCommand::class.java)
        for (r in reflections) {
            cmdList + r.newInstance().syntaxDescription() + "\n"
        }
        return cmdList
    }

    override fun run(event: GuildMessageReceivedEvent) {
        event.channel.sendMessage(getCommandList()).queue()
    }

}