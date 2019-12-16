package com.portalsoup.mrleaguy

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.apache.commons.collections4.queue.CircularFifoQueue

class EventListener(val bot: MrButlertron) : ListenerAdapter() {

    val history: Array<String> = arrayOf("","","","","","","","","","")
    var currentIndex = 0

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (history.contains(event.message.id)) {
            println("Found a duplicate!  ${event.message.id}")
            return
        } else {
            println("Adding ${event.message.id} to index=${currentIndex}")
            history[currentIndex] = event.message.id
            incrementHistoryCounter()
        }

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

    fun incrementHistoryCounter() {
        if (currentIndex >= history.size - 1) {
            currentIndex = 0
        } else {
            currentIndex++
        }
    }
}