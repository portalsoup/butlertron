package com.portalsoup.mrbutlertron

import com.portalsoup.mrbutlertron.commands.guildjoin.usercommands.GreetNewMembers
import com.portalsoup.mrbutlertron.commands.messagereceived.usercommands.*
import com.portalsoup.mrbutlertron.core.command.AbstractCommand
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.Event

class MrButlertron(private val token: String) {

    val commands: List<AbstractCommand<out Event>> = listOf(
        MagicCardLookup(),
        YugiohCardLookup(),
        CommandList(),
        GreetNewMembers()
    )

    fun run() {
        val jdaBuilder = JDABuilder(AccountType.BOT)
            .addEventListeners(EventListener(this))
            .setToken(token)
            .setAutoReconnect(true)

        jdaBuilder.build().awaitReady()
    }

}