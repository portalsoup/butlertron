package com.portalsoup.mrbutlertron.commands.guildjoin

import com.portalsoup.mrbutlertron.core.command.AbstractCommand
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.guild.GuildJoinEvent

abstract class GuildJoinCommand(commandName: String) : AbstractCommand<GuildJoinEvent>(commandName) {

    /**
     * @return true if the provided event matches this command's predicates
     */
    override fun shouldRun(event: GuildJoinEvent) : Boolean {
        return try {

            val channel: TextChannel = event.guild.defaultChannel ?: return false

            if (!hasPermissionToPost(event.guild.selfMember, channel)) {
                return false
            }
            return runPredicate(event)
        } catch (e: Exception) {
            println(unexpectedFailureMessage + ": " + e.message + "\n")
            e.printStackTrace()
            false
        }
    }

    override fun doRun(event: GuildJoinEvent) =
        try {
            run(event)
        } catch (e: Exception) {
            println(unexpectedFailureMessage + ": " + e.message)
        }
}