package com.portalsoup.mrbutlertron.v2

import com.portalsoup.mrbutlertron.v2.core.CommandLoader
import com.portalsoup.mrbutlertron.v2.core.getLogger
import com.portalsoup.mrbutlertron.v2.data.DatabaseFactory
import net.dv8tion.jda.api.JDABuilder

class Bot(val name: String, val token: String) {

    private val log = getLogger(javaClass)

    val jda by lazy {
        val token = token
        log.debug("token: ${token}")

        JDABuilder.createDefault(token)
            .addEventListeners(CommandLoader(this))
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