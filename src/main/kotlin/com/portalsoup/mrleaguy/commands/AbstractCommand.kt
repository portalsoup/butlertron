package com.portalsoup.mrleaguy.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent


abstract class AbstractCommand {

    /**
     * @return true if the provided event matches this command's predicates
     */
    open fun handle(event: GuildMessageReceivedEvent) : Boolean {
        val message = event.message.contentRaw

        return when {
            runPredicate(event) -> {
                run(event)
                true
            }
            else -> false
        }
    }

    abstract fun runPredicate(event: GuildMessageReceivedEvent) : Boolean
    abstract fun run(event: GuildMessageReceivedEvent)
}