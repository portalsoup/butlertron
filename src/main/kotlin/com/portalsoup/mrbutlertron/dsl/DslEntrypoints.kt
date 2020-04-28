package com.portalsoup.discordbot.core.command

import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent

abstract class AbstractCommand<E : Event>(val eventType: SupportedEvents, val command: Command<E>)

abstract class GuildMessageReceivedCommand<E : GuildMessageReceivedEvent>(command: Command<E>)
    : AbstractCommand<E>(SupportedEvents.GUILD_MESSAGE_RECEIVED, command)

abstract class GuildJoinCommand<E : GuildMemberJoinEvent>(command: Command<E>)
    : AbstractCommand<E>(SupportedEvents.GUILD_JOIN, command)

abstract class PrivateMessageReceivedCommand<E : PrivateMessageReceivedEvent>(command: Command<E>)
    : AbstractCommand<E>(SupportedEvents.PRIVATE_MESSAGE_RECEIVED, command)

enum class SupportedEvents {
    GUILD_MESSAGE_RECEIVED,
    PRIVATE_MESSAGE_RECEIVED,
    GUILD_JOIN,
}