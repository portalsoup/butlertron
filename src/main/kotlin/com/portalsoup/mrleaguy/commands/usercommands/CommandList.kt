package com.portalsoup.mrleaguy.commands.usercommands

import com.portalsoup.mrleaguy.commands.AbstractCommand
import com.portalsoup.mrleaguy.commands.ApiCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.reflections.Reflections

class CommandList : AbstractCommand() {

    override fun syntaxDescription(): String = "\"help mr butlertron\" or \"help mr. butlertron\""

    override fun runPredicate(event: GuildMessageReceivedEvent): Boolean {
        val sanitized = event.message.contentRaw.trim().toLowerCase()
        return sanitized == "help mr butlertron" || sanitized == "help mr. butlertron"
    }

    fun getCommandList(): String {
        try {
            val cmdList = ""
            val reflections = Reflections().getSubTypesOf(AbstractCommand::class.java)
                .filter { c: Class<out AbstractCommand> -> c.simpleName != AbstractCommand::class.java.simpleName }
                .filter { c: Class<out AbstractCommand> -> c.simpleName != ApiCommand::class.java.simpleName }

            println("Found ${reflections.size} commands")
            for (r in reflections) {
                println("from r=${r.simpleName} from java=${AbstractCommand::class.java.simpleName}")
                print("Instantiating ${r.simpleName}... ")
                cmdList + r.newInstance().syntaxDescription() + "\n"
                println("Instantiated")
            }
            println("cmdList=$cmdList")
            return cmdList
        } catch (e: RuntimeException) {
            println("Failed: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    override fun run(event: GuildMessageReceivedEvent) {
        event.channel.sendMessage(getCommandList()).queue()
    }

}