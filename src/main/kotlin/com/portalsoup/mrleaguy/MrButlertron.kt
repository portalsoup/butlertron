package com.portalsoup.mrleaguy

import com.portalsoup.mrleaguy.commands.AbstractCommand
import com.portalsoup.mrleaguy.commands.usercommands.CommandList
import com.portalsoup.mrleaguy.commands.usercommands.MagicLookup
import com.portalsoup.mrleaguy.commands.usercommands.YugiohLookup
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder

class MrButlertron(private val token: String) {

    val commands: List<AbstractCommand> = listOf(
        MagicLookup(),
        YugiohLookup(),
        CommandList()
    )

    fun run() {
        val jdaBuilder = JDABuilder(AccountType.BOT)
            .addEventListeners(EventListener(this))
            .setToken(token)
            .setAutoReconnect(true)

        jdaBuilder.build().awaitReady()
    }
}