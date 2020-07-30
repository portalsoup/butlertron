package com.portalsoup.mrbutlertron.bot

import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder

object DiscordBot {
    val global by lazy {
        val token = System.getProperty("butlertron.token")
//        token.replace("\"","")
        println("token: ${token}")

        JDABuilder(AccountType.BOT)
            .addEventListeners(EventListener(this))
            .setToken(token)
            .setAutoReconnect(true)
            .build()
            .awaitReady()
    }
}