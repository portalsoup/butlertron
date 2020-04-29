package com.portalsoup.mrbutlertron

import com.portalsoup.mrbutlertron.bot.DiscordBot
import com.portalsoup.mrbutlertron.data.DatabaseFactory
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException

fun main(args: Array<String>) {
    val flyway = Flyway.configure().dataSource(DatabaseFactory.dataSource, "bot", null).load()
    migrateFlyway(flyway)
    DatabaseFactory.init()

    println("Loading ENV")
    val token = System.getenv("BOT_TOKEN")
    token.replace("\"","")
    println("token: ${token}")

    println("Starting bot")
    DiscordBot.global
}

fun migrateFlyway(flyway: Flyway, runAgain: Boolean = true) {
    try {
        flyway.migrate()
    } catch (e: FlywayException) {
        flyway.repair()
        if (runAgain) {
            migrateFlyway(flyway, false)
        } else {
            throw e
        }
    }
}