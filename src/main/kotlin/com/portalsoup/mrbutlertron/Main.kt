package com.portalsoup.mrbutlertron

import com.portalsoup.mrbutlertron.Environment.name
import com.portalsoup.mrbutlertron.Environment.token
import com.portalsoup.mrbutlertron.bot.DiscordBot
import java.lang.RuntimeException

fun main(args: Array<String>) {
    DiscordBot(name, token).apply {
        init()
        run()
    }
}

object Environment {
    val nookpediaToken = System.getProperty("nookipedia.token") ?: throw RuntimeException("Missing nookipedia token")
    val name = System.getProperty("discord.bot.name") ?: throw RuntimeException("Missing java property [discord.bot.name]")
    val token = System.getProperty("discord.bot.token") ?: throw RuntimeException("Missing java property [discord.bot.token]")
    val githubUrl: String? = System.getProperty("github.url")
}