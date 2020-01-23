package com.portalsoup.mrbutlertron

import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder

class MrButlertron(private val token: String) {

    fun run() {
        val jdaBuilder = JDABuilder(AccountType.BOT)
            .addEventListeners(EventListener(this))
            .setToken(token)
            .setAutoReconnect(true)

        jdaBuilder.build().awaitReady()
    }

}