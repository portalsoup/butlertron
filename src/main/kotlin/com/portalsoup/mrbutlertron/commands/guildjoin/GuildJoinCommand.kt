package com.portalsoup.mrbutlertron.commands.guildjoin

import com.portalsoup.mrbutlertron.core.command.AbstractCommand
import net.dv8tion.jda.api.events.guild.GuildJoinEvent

abstract class GuildJoinCommand : AbstractCommand<GuildJoinEvent>() {

    init {
        addPrecondition { event ->
            event.guild.defaultChannel.let { maybeChannel ->
                if (maybeChannel != null) {
                    hasPermissionToPost(event.guild.selfMember, maybeChannel)
                }  else {
                    false
                }
            }
        }
    }
}