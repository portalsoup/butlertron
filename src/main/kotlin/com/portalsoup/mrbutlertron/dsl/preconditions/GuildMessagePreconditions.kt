package com.portalsoup.discordbot.core.command.preconditions

import com.portalsoup.discordbot.core.command.CommandDsl
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent
import net.dv8tion.jda.api.events.message.priv.GenericPrivateMessageEvent

@CommandDsl
class GuildMessagePreconditions<E: GenericGuildMessageEvent>(val preconditions: MutableList<(E) -> Boolean>) {

    private fun sanitize(event: GenericGuildMessageEvent): String {
        return event
            .channel
            .retrieveMessageById(event.messageId)
            .complete()
            .contentRaw
            .trim()
    }

    fun beginsWith(lambda: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                sanitize(event).startsWith(lambda())
            } catch (exception: Throwable) {
                false
            }
        }

    fun matches(regex: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                sanitize(event).matches(Regex(regex()))
            } catch (exception: Throwable) {
                false
            }
        }

    fun matchesToLowercase(regex: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                sanitize(event).toLowerCase().matches(Regex(regex()))
            } catch (exception: Throwable) {
                false
            }
        }

    fun equals(message: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                sanitize(event) == message()
            } catch (exception: Throwable) {
                false
            }
        }

    fun equalsIgnoreCase(message: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                sanitize(event).toLowerCase() == message().toLowerCase()
            } catch (exception: Throwable) {
                false
            }
        }
}

@CommandDsl
class GuildMessageAuthorPreconditions<E: GenericGuildMessageEvent>(val preconditions: MutableList<(E) -> Boolean>) {

    private fun sanitize(event: GenericGuildMessageEvent): String {
        return event
            .channel
            .retrieveMessageById(event.messageId)
            .complete()
            .author
            .name
            .trim()
    }

    fun beginsWith(lambda: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                sanitize(event).startsWith(lambda())
            } catch (exception: Throwable) {
                false
            }
        }

    fun matches(regex: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                sanitize(event).matches(Regex(regex()))
            } catch (exception: Throwable) {
                false
            }
        }

    fun equals(message: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                sanitize(event) == message()
            } catch (exception: Throwable) {
                false
            }
        }

    fun equalsIgnoreCase(message: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                sanitize(event).toLowerCase() == message().toLowerCase()
            } catch (exception: Throwable) {
                false
            }
        }
}