package com.portalsoup.mrbutlertron.commands.dsl

import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

abstract class AbstractDslCommand<E : Event>(val eventType: SupportedEvents, val command: Command<E>)

abstract class MessageReceivedDslCommand<E : GuildMessageReceivedEvent>(command: Command<E>)
    : AbstractDslCommand<E>(SupportedEvents.MESSAGE_RECEIVED, command)

abstract class GuildJoinDslCommand<E : GuildJoinEvent>(command: Command<E>)
    : AbstractDslCommand<E>(SupportedEvents.MESSAGE_RECEIVED, command)

enum class SupportedEvents {
    MESSAGE_RECEIVED
}