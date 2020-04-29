package com.portalsoup.mrbutlertron

import com.portalsoup.mrbutlertron.bot.DiscordBot
import com.portalsoup.mrbutlertron.data.DatabaseFactory
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException

fun main(args: Array<String>) {
    DatabaseFactory.init()

    println("Loading ENV")
    val token = System.getenv("BOT_TOKEN")
    token.replace("\"","")
    println("token: ${token}")

    println("Starting bot")
    DiscordBot.global
}