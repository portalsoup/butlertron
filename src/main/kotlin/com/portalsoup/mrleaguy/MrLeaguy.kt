package com.portalsoup.mrleaguy

import com.portalsoup.mrleaguy.commands.AbstractCommand
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder

class MrLeaguy {

    val commands = listOf<AbstractCommand>()

    fun run() {
        val jdaBuilder = JDABuilder(AccountType.BOT)


    }
}