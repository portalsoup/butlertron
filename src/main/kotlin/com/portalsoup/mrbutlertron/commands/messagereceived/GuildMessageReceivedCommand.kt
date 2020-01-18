package com.portalsoup.mrbutlertron.commands.messagereceived

import com.portalsoup.mrbutlertron.core.command.AbstractCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

abstract class GuildMessageReceivedCommand(commandName: String) :
    AbstractCommand<GuildMessageReceivedEvent>(commandName) {

    /**
     * @return true if the provided event matches this command's predicates
     */
    override fun shouldRun(event: GuildMessageReceivedEvent) : Boolean {
        return try {

            if (!hasPermissionToPost(event.guild.selfMember, event.channel)) {
                return false
            }
            return runPredicate(event)
        } catch (e: Exception) {
            println(unexpectedFailureMessage + ": " + e.message + "\n")
            e.printStackTrace()
            false
        }
    }

    override fun doRun(event: GuildMessageReceivedEvent) =
        try {
            run(event)
        } catch (e: Exception) {
            println(unexpectedFailureMessage + ": " + e.message)
        }
}