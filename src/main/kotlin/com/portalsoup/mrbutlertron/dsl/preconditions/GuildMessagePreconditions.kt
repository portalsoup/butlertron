package com.portalsoup.discordbot.core.command.preconditions

import com.portalsoup.discordbot.core.command.CommandDsl
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent
import net.dv8tion.jda.api.events.message.priv.GenericPrivateMessageEvent

@CommandDsl
class GuildMessagePreconditions<E: GenericGuildMessageEvent>(val preconditions: MutableList<(E) -> Boolean>) {

    fun beginsWith(lambda: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                event
                    .channel
                    .retrieveMessageById(event.messageId)
                    .complete()
                    .contentRaw
                    .trim()
                    .startsWith(lambda())
            } catch (exception: Throwable) {
                false
            }
        }

    fun matches(regex: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                event
                    .channel
                    .retrieveMessageById(event.messageId)
                    .complete()
                    .contentRaw.trim()
                    .matches(Regex(regex()))
            } catch (exception: Throwable) {
                false
            }
        }

    fun equals(message: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                event
                    .channel
                    .retrieveMessageById(event.messageId)
                    .complete()
                    .contentRaw.trim()
                    .equals(message())
            } catch (exception: Throwable) {
                false
            }
        }

    fun equalsIgnoreCase(message: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                event
                    .channel
                    .retrieveMessageById(event.messageId)
                    .complete()
                    .contentRaw.trim()
                    .toLowerCase()
                    .equals(message().toLowerCase())
            } catch (exception: Throwable) {
                false
            }
        }
}

@CommandDsl
class GuildMessageAuthorPreconditions<E: GenericGuildMessageEvent>(val preconditions: MutableList<(E) -> Boolean>) {

    fun beginsWith(lambda: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                event
                    .channel
                    .retrieveMessageById(event.messageId)
                    .complete()
                    .author
                    .name
                    .startsWith(lambda())
            } catch (exception: Throwable) {
                false
            }
        }

    fun matches(regex: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                event
                    .channel
                    .retrieveMessageById(event.messageId)
                    .complete()
                    .author
                    .name
                    .matches(Regex(regex()))
            } catch (exception: Throwable) {
                false
            }
        }

    fun equals(message: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                event
                    .channel
                    .retrieveMessageById(event.messageId)
                    .complete()
                    .author
                    .name
                    .equals(message())
            } catch (exception: Throwable) {
                false
            }
        }

    fun equalsIgnoreCase(message: () -> String) =
        preconditions.add { event: GenericGuildMessageEvent ->
            try {
                event
                    .channel
                    .retrieveMessageById(event.messageId)
                    .complete()
                    .contentRaw.trim()
                    .toLowerCase()
                    .equals(message().toLowerCase())
            } catch (exception: Throwable) {
                false
            }
        }
}