package com.portalsoup.mrbutlertron.bot

import com.portalsoup.mrbutlertron.core.getLogger
import com.portalsoup.mrbutlertron.data.DatabaseFactory
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

class DiscordBot(val name: String, val token: String) {
    private val log = getLogger(javaClass)

    val jda by lazy {
        val token = token
        log.debug("token: ${token}")

        JDABuilder.createDefault(token)
            .addEventListeners(EventListener(this))
            .setAutoReconnect(true)
            .build()
            .awaitReady()
    }

    fun init() {
        log.info("Initializing $name...")
        DatabaseFactory.init()
    }

    fun run() {
        jda // initialize it now
        log.info("$name ready for service!")
    }
}