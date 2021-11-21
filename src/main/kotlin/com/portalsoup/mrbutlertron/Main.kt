package com.portalsoup.mrbutlertron

import com.portalsoup.mrbutlertron.Environment.name
import com.portalsoup.mrbutlertron.Environment.token
import com.portalsoup.mrbutlertron.v2.Bot

fun main(args: Array<String>) {

    Bot(name, token).apply {
        init()
        run()
    }
}

// TODO make this not include secrets
object Environment {
    val nookpediaToken = System.getProperty("nookipedia.token") ?: throw RuntimeException("Missing nookipedia token")
    val name = System.getProperty("discord.bot.name") ?: throw RuntimeException("Missing java property [discord.bot.name]")
    val token = System.getProperty("discord.bot.token") ?: throw RuntimeException("Missing java property [discord.bot.token]")
    val githubUrl: String? = System.getProperty("github.url")
    val commandPrefix: String = System.getProperty("command.prefix")
}