package com.portalsoup.mrbutlertron.commands.messagereceived.usercommands

import com.portalsoup.mrbutlertron.commands.messagereceived.GuildMessageReceivedCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class ButlertronQuotes : GuildMessageReceivedCommand("Butlertron Quotes") {

    override fun runPredicate(event: GuildMessageReceivedEvent): Boolean {
        return runIf(event.message.contentRaw)
    }

    fun runIf(message: String): Boolean {
        val sanitized = message.trim().toLowerCase()
        return  sanitized.contains("mr") &&
                sanitized.contains("butlertron")
    }

    override fun run(event: GuildMessageReceivedEvent) {
        val quote = listOf(
            "I'm sorry, Wesley, you have ADD.",
            "Perhaps you could get Clone High a corporate sponsor. Those Pumas were rather fresh.",
            "Oh Wesley. At least I'm not a pompous china dog whose evil plans suck the devils ASS. That's right WESLEY. Find yourself a new best FRIEND.",
            "Your friend should listen to her heart. I'm not programmed to wink but if I were programmed to wink I would have winked when I said your friend.",
            "Penny for your thoughts.",
            "Are you A: handsome; B: smart; C: scrap metal; or D: all of the above?",
            "The answer is C... you fuckwad.",
            "Shouldn't you be saving some of this money for your secret plan instead of having me gold-plated and lowered?",
            "Anyone for scooones?",
            "More tea, Wesley?",
            "Weeeesleeey",
            "Maybe some overpriced knick-knacks will add a touch of class."
        ).random()

        event.channel.sendMessage(quote).queue()
    }
}