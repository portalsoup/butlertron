package com.portalsoup.mrbutlertron

import com.portalsoup.mrbutlertron.commands.guildjoin.usercommands.GreetNewMembers
import com.portalsoup.mrbutlertron.commands.messagereceived.usercommands.ButlertronQuotes
import com.portalsoup.mrbutlertron.commands.messagereceived.usercommands.CommandList
import com.portalsoup.mrbutlertron.commands.messagereceived.usercommands.MagicCardLookup
import com.portalsoup.mrbutlertron.commands.messagereceived.usercommands.YugiohCardLookup
import com.portalsoup.mrbutlertron.core.command.AbstractCommand
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder

class MrButlertron(private val token: String) {

    val commands: List<AbstractCommand<*>> = listOf(
        MagicCardLookup(),
        YugiohCardLookup(),
        CommandList(),
        ButlertronQuotes(),
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