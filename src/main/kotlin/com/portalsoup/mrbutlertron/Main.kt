package com.portalsoup.mrbutlertron

import com.portalsoup.mrbutlertron.bot.DiscordBot
import java.lang.RuntimeException

fun main(args: Array<String>) {
    val name = System.getProperty("discord.bot.name") ?: throw RuntimeException("Missing java property [discord.bot.name]")
    val token = System.getProperty("discord.bot.token") ?: throw RuntimeException("Missing java property [discord.bot.token]")

    DiscordBot(name, token).apply {
        init()
        run()
    }
}