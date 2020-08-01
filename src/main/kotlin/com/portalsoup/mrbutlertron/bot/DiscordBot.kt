package com.portalsoup.mrbutlertron.bot

import com.portalsoup.mrbutlertron.core.getLogger
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder

object DiscordBot {
    private val log = getLogger(javaClass)

    val global by lazy {
        val token = System.getProperty("butlertron.token")
        log.debug("token: ${token}")

        JDABuilder(AccountType.BOT)
            .addEventListeners(EventListener(this))
            .setToken(token)
            .setAutoReconnect(true)
            .build()
            .awaitReady()
    }
}