package com.portalsoup.discordbot.core.command.type

import com.portalsoup.discordbot.core.command.AbstractCommandBuilder
import com.portalsoup.discordbot.core.command.CommandDsl
import com.portalsoup.discordbot.core.command.JobBuilder
import com.portalsoup.discordbot.core.command.PreconditionListBuilder
import com.portalsoup.discordbot.core.command.preconditions.PrivateMessageAuthorPreconditions
import com.portalsoup.discordbot.core.command.preconditions.PrivateMessagePreconditions
import com.portalsoup.mrbutlertron.core.getLogger
import net.dv8tion.jda.api.events.message.priv.GenericPrivateMessageEvent

/**
 * Top level entrypoint
 */
fun <E : GenericPrivateMessageEvent> sendDM(lambda: PrivateMessageReceivedCommandBuilder<E>.() -> Unit) =
    PrivateMessageReceivedCommandBuilder<E>().apply(lambda).build()

@CommandDsl
class PrivateMessageReceivedCommandBuilder<E : GenericPrivateMessageEvent> : AbstractCommandBuilder<E>() {

    fun job(lambda: PrivateMessageJobBuilder<E>.() -> Unit) {
        job = PrivateMessageJobBuilder<E>().apply(lambda).build()
    }

    fun preconditions(lambda: PrivateMessagePreconditionListBuilder<E>.() -> Unit) {
        preconditions.addAll(
            PrivateMessagePreconditionListBuilder<E>().apply(lambda).build()
        )
    }
}

@CommandDsl
open class PrivateMessageJobBuilder<E : GenericPrivateMessageEvent> : JobBuilder<E>() {
    private val log = getLogger(javaClass)
    fun reply(lambda: () -> String) {
        addRunner { event ->
            val message = lambda()
            log.info("Sending a message to a private user. $message ")
            event.channel.sendMessage(message).queue()

        }
    }
}

class PrivateMessagePreconditionListBuilder<E : GenericPrivateMessageEvent> : PreconditionListBuilder<E>() {
    fun message(lambda: PrivateMessagePreconditions<E>.() -> Unit) =
        PrivateMessagePreconditions(preconditions).apply(lambda)

    fun sender(lambda: PrivateMessageAuthorPreconditions<E>.() -> Unit) =
        PrivateMessageAuthorPreconditions(preconditions).apply(lambda)
}