package com.portalsoup.mrleaguy

import com.portalsoup.mrleaguy.commands.HelloWorld
import com.portalsoup.mrleaguy.commands.MagicLookup
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder

class MrButlertron(private val token: String) {

    val commands = listOf(HelloWorld(), MagicLookup())

    fun run() {
        val jdaBuilder = JDABuilder(AccountType.BOT)
            .addEventListeners(EventListener(this))
            .setToken(token)
            .setAutoReconnect(true)

        jdaBuilder.build().awaitReady()
    }
}