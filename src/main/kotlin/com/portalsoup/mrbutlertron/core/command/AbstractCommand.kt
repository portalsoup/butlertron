package com.portalsoup.mrbutlertron.core.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.Event


abstract class AbstractCommand<in E : Event>(val commandName: String) {

    val unexpectedFailureMessage = "Caught an unexpected message"

    abstract fun shouldRun(event: E) : Boolean
    abstract fun doRun(event: E)

    // These two must only be used here to make sure no exceptions kill the bot
    protected abstract fun runPredicate(event: E) : Boolean
    protected abstract fun run(event: E)

    // helpers
    fun prefixPredicate(full: String, prefix: String): Boolean {
        return full
            .toLowerCase()
            .trim()
            .startsWith(prefix)
    }


    fun hasPermissionToPost(self: Member, channel: TextChannel) : Boolean {
        return self.hasPermission(channel, Permission.MESSAGE_WRITE)
    }
}