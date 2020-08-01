package com.portalsoup.mrbutlertron

import com.portalsoup.mrbutlertron.bot.DiscordBot
import com.portalsoup.mrbutlertron.core.getLogger
import com.portalsoup.mrbutlertron.data.DatabaseFactory
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val butlertron = MrButlertron()

    butlertron.apply {
        init()
        run()
    }
}

class MrButlertron {
    private val log = getLogger(javaClass)

    fun init() {
        log.info("Initializing Mr. Butlertron...")
        DatabaseFactory.init()
    }

    fun run() {
        DiscordBot.global
        log.info("Mr. Butlertron ready for service!")
    }
}