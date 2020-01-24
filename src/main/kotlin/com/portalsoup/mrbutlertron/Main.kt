package com.portalsoup.mrbutlertron

import com.portalsoup.discordbot.DiscordBot

fun main(args: Array<String>) {
    println("Loading ENV")
    val token = System.getenv("BOT_TOKEN")
    token.replace("\"","")
    println("token: ${token}")

    println("Starting bot")
    val bot = DiscordBot(token)
    bot.run()
}