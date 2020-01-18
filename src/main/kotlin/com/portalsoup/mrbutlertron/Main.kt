package com.portalsoup.mrbutlertron

fun main(args: Array<String>) {
    println("Loading ENV")
    val token = System.getenv("BOT_TOKEN")
    token.replace("\"","")
    println("token: ${token}")

    println("Starting bot")
    val bot = MrButlertron(token)
    bot.run()
}