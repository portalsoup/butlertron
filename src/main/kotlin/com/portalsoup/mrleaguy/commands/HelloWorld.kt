package com.portalsoup.mrleaguy.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class HelloWorld : AbstractCommand() {

    override fun runPredicate(event: GuildMessageReceivedEvent): Boolean {
        return event.message.contentRaw.toLowerCase().startsWith("mr leaguy")
    }

    override fun run(event: GuildMessageReceivedEvent) {
        event.channel.sendMessage("Hello, ${event.message.author.name}!")
    }
}