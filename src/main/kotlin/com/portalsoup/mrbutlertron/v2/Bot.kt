package com.portalsoup.mrbutlertron.v2

import butlerQuotes
import com.portalsoup.mrbutlertron.v2.core.CommandLoader
import com.portalsoup.mrbutlertron.v2.core.getLogger
import com.portalsoup.mrbutlertron.v2.data.DatabaseFactory
import embedTest
import friendCode
import mtg
import net.dv8tion.jda.api.JDABuilder
import norrisJokes
import pokedex
import shinChan
import villagerLookup
import ygo

class Bot(val name: String, val token: String) {

    private val discordCommands = CommandLoader(
        this, setOf(
            villagerLookup,
            butlerQuotes,
            embedTest,
            friendCode,
            mtg,
            norrisJokes,
            pokedex,
            shinChan,
            ygo
        )
    )

    private val log = getLogger(javaClass)

    val jda by lazy {
        val token = token
        log.debug("token: ${token}")

        JDABuilder.createDefault(token)
            .addEventListeners(discordCommands)
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