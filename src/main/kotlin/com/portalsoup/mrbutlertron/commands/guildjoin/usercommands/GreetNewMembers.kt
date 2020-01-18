package com.portalsoup.mrbutlertron.commands.guildjoin.usercommands

import com.portalsoup.mrbutlertron.commands.guildjoin.GuildJoinCommand
import net.dv8tion.jda.api.events.guild.GuildJoinEvent

class GreetNewMembers : GuildJoinCommand("New Member Greeting") {

    override fun runPredicate(event: GuildJoinEvent): Boolean {
        return false
    }

    override fun run(event: GuildJoinEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}