package com.portalsoup.mrbutlertron.commands.guildjoin.usercommands

import com.portalsoup.mrbutlertron.commands.guildjoin.GuildJoinCommand
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.guild.GuildJoinEvent

class GreetNewMembers : GuildJoinCommand() {

    override fun run(event: GuildJoinEvent) {
        val members = event.guild.members
        members.sortWith<Member>(Comparator { o1, o2 -> o1.timeJoined.compareTo(o2.timeJoined) }) // TODO this may be comparing backwards

        val memberName = members.last().effectiveName

        val channel = event.guild.getTextChannelsByName("bot-testing-ground", false).firstOrNull() ?: return

        channel.sendMessage("Hello, ${memberName}!").queue()
    }
}