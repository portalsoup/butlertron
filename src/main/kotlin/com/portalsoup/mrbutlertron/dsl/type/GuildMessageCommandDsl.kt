package com.portalsoup.discordbot.core.command.type

import com.portalsoup.discordbot.core.command.AbstractCommandBuilder
import com.portalsoup.discordbot.core.command.CommandDsl
import com.portalsoup.discordbot.core.command.JobBuilder
import com.portalsoup.discordbot.core.command.PreconditionListBuilder
import com.portalsoup.discordbot.core.command.preconditions.GuildMessageAuthorPreconditions
import com.portalsoup.discordbot.core.command.preconditions.GuildMessagePreconditions
import com.portalsoup.mrbutlertron.Environment
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent

@CommandDsl
class GuildMessageCommandBuilder<E : GenericGuildMessageEvent> : AbstractCommandBuilder<E>() {

    fun job(lambda: GuildMessageJobBuilder<E>.() -> Unit) {
        job = GuildMessageJobBuilder<E>().apply(lambda).build()
    }

    fun preconditions(lambda: GuildMessagePreconditionListBuilder<E>.() -> Unit) {
        preconditions.addAll(
            GuildMessagePreconditionListBuilder<E>().apply(lambda).build()
        )
    }
}

@CommandDsl
class GuildMessagePreconditionListBuilder<E : GenericGuildMessageEvent> : PreconditionListBuilder<E>() {

    fun message(lambda: GuildMessagePreconditions<E>.() -> Unit) =
        GuildMessagePreconditions(preconditions).apply(lambda)

    fun sender(lambda: GuildMessageAuthorPreconditions<E>.() -> Unit) =
        GuildMessageAuthorPreconditions(preconditions).apply(lambda)
}


@CommandDsl
open class GuildMessageJobBuilder<E : GenericGuildMessageEvent> : JobBuilder<E>() {
    fun reply(lambda: (E) -> String) = addRunner { it.channel.sendMessage(lambda(it)).queue() }

    fun replyDM(lambda: () -> String) {
        addRunner { event ->
            event.channel
                .retrieveMessageById(event.messageId)
                .complete()
                .author
                .openPrivateChannel()
                .queue() {
                    it.sendMessage(lambda()).queue()
                }
        }
    }

}

fun <E : GenericGuildMessageEvent> sendMessage(lambda: GuildMessageCommandBuilder<E>.() -> Unit) =
    GuildMessageCommandBuilder<E>().apply(lambda).build()
