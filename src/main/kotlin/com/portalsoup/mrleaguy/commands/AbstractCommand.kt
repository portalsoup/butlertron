package com.portalsoup.mrleaguy.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent


abstract class AbstractCommand {

    val unexpectedFailureMessage = "Caught an unexpected message"

    /**
     * @return true if the provided event matches this command's predicates
     */
    open fun shouldRun(event: GuildMessageReceivedEvent) : Boolean {
        return try {
            val guild = event.guild
            val channel = event.channel

            val permissionToPost = guild.selfMember.hasPermission(channel, Permission.MESSAGE_WRITE)

            if (!permissionToPost) {
                return false
            }
            return runPredicate(event)
        } catch (e: Exception) {
            println(unexpectedFailureMessage + ": " + e.message + "\n")
            e.printStackTrace()
            false
        }
    }

    open fun doRun(event: GuildMessageReceivedEvent) =
        try {
            run(event)
        } catch (e: Exception) {
            println(unexpectedFailureMessage + ": " + e.message)
        }


    // These two must only be used here to make sure no exceptions kill the bot
    protected abstract fun runPredicate(event: GuildMessageReceivedEvent) : Boolean
    protected abstract fun run(event: GuildMessageReceivedEvent)

    // helpers
    fun prefixPredicate(full: String, prefix: String): Boolean {
        return full
            .toLowerCase()
            .trim()
            .startsWith(prefix)
    }
}