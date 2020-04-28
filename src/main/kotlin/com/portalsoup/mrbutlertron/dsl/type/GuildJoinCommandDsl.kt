package com.portalsoup.discordbot.core.command.type

import com.portalsoup.discordbot.core.command.AbstractCommandBuilder
import com.portalsoup.discordbot.core.command.JobBuilder
import com.portalsoup.discordbot.core.command.preconditions.GuildJoinPreconditions
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import java.lang.RuntimeException

fun <E : GuildMemberJoinEvent> onJoin(lambda: GuildJoinCommandBuilder<E>.() -> Unit) =
    GuildJoinCommandBuilder<E>().apply(lambda).build()

class GuildJoinCommandBuilder<E : GuildMemberJoinEvent> : AbstractCommandBuilder<E>() {

    fun job(lambda: GuildJoinJobBuilder<E>.() -> Unit) {
        job = GuildJoinJobBuilder<E>().apply(lambda).build()
    }

    // preconditions
    fun preconditions(lambda: GuildJoinPreconditions<E>.() -> Unit) {
            GuildJoinPreconditions<E>(preconditions).apply(lambda)
    }
}

class GuildJoinJobBuilder<E : GuildMemberJoinEvent> : JobBuilder<E>() {

    fun sendMessage(lambda: () -> String) {
        addRunner { event ->
            val channel = event.guild.defaultChannel ?: throw RuntimeException("No default channel found for guild: ${event.guild.name}")
            channel.sendMessage(lambda()).queue()
        }
    }

    fun sendDM(lambda: () -> String) {
        addRunner { event ->
            event.member.user.openPrivateChannel().queue() {
                it.sendMessage(lambda()).queue()
            }
        }
    }
}