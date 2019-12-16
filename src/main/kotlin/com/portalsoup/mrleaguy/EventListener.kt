package com.portalsoup.mrleaguy

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventListener(val bot: MrButlertron) : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val guild = event.guild
        val channel = event.channel
        val permissionToPost = guild.selfMember.hasPermission(channel, Permission.MESSAGE_WRITE)

        if (event.author.isBot || !permissionToPost) {
            return
        }

        for (command in bot.commands) {
            if (command.shouldRun(event)) {
                command.doRun(event)
            }
        }
    }
}