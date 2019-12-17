package com.portalsoup.mrleaguy

import com.portalsoup.mrleaguy.commands.AbstractCommand
import com.portalsoup.mrleaguy.commands.usercommands.ButlertronQuotes
import com.portalsoup.mrleaguy.commands.usercommands.CommandList
import com.portalsoup.mrleaguy.commands.usercommands.MagicCardLookup
import com.portalsoup.mrleaguy.commands.usercommands.YugiohCardLookup
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder

class MrButlertron(private val token: String) {

    val commands: List<AbstractCommand> = listOf(
        MagicCardLookup(),
        YugiohCardLookup(),
        CommandList(),
        ButlertronQuotes()
    )

    fun run() {
        val jdaBuilder = JDABuilder(AccountType.BOT)
            .addEventListeners(EventListener(this))
            .setToken(token)
            .setAutoReconnect(true)

        jdaBuilder.build().awaitReady()
    }
}