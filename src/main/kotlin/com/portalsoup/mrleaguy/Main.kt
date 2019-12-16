package com.portalsoup.mrleaguy

import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder

fun main(args: Array<String>) {
    println("Loading ENV")
    val token = System.getenv("BOT_TOKEN")

    println("Starting bot")
    val bot = MrButlertron(token)
    bot.run()
}