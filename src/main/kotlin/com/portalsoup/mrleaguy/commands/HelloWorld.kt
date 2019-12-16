package com.portalsoup.mrleaguy.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class HelloWorld : AbstractCommand() {

    override fun runPredicate(event: GuildMessageReceivedEvent): Boolean {
        return prefixPredicate(event.message.contentRaw, "mr butlertron")
    }

    override fun run(event: GuildMessageReceivedEvent) {
        event.channel.sendMessage("Hello...").queue()
        Thread.sleep(1000)
        event.channel.sendMessage("Wesley!").queue()
    }
}