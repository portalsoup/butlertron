package com.portalsoup.mrbutlertron.commands.messagereceived

import com.portalsoup.mrbutlertron.core.command.AbstractCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

abstract class GuildMessageReceivedCommand : AbstractCommand<GuildMessageReceivedEvent>() {

    init {
        addPrecondition { hasPermissionToPost(it.guild.selfMember, it.channel) }
    }
}