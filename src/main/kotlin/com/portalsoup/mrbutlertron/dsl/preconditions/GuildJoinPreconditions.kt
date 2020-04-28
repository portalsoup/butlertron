package com.portalsoup.discordbot.core.command.preconditions

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent

class GuildJoinPreconditions<E : GuildMemberJoinEvent>(val preconditions: MutableList<(E) -> Boolean>) {
    fun isABot(lambda: () -> Boolean) =
        preconditions.add { event: GuildMemberJoinEvent ->
            try {
                event.user.isBot == lambda()
            } catch(exception: Throwable) {
                false
            }
        }

    fun name(lambda: GuildJoinNamePreconditions<E>.() -> Unit) =
        GuildJoinNamePreconditions<E>(preconditions).apply(lambda)

    fun event(lambda: (event: GuildMemberJoinEvent) -> Boolean) =
        preconditions.add {
            try {
                lambda(it)
            } catch (exception: Throwable) {
                false
            }
        }
}

class GuildJoinNamePreconditions<E : GuildMemberJoinEvent>(val preconditions: MutableList<(E) -> Boolean>) {
    fun beginsWith(lambda: () -> String) =
        preconditions.add { event: GuildMemberJoinEvent ->
            try {
                event
                    .member
                    .effectiveName
                    .trim()
                    .startsWith(lambda())
            } catch (exception: Throwable) {
                false
            }
        }

    fun matches(regex: () -> String) =
        preconditions.add { event: GuildMemberJoinEvent ->
            try {
                event
                    .member
                    .effectiveName
                    .trim()
                    .matches(Regex(regex()))
            } catch (exception: Throwable) {
                false
            }
        }

    fun equals(message: () -> String) =
        preconditions.add { event: GuildMemberJoinEvent ->
            try {
                event
                    .member
                    .effectiveName
                    .trim()
                    .equals(message())
            } catch (exception: Throwable) {
                false
            }
        }

    fun equalsIgnoreCase(message: () -> String) =
        preconditions.add { event: GuildMemberJoinEvent ->
            try {
                event
                    .member
                    .effectiveName
                    .trim()
                    .toLowerCase()
                    .equals(message().toLowerCase())
            } catch (exception: Throwable) {
                false
            }
        }
}