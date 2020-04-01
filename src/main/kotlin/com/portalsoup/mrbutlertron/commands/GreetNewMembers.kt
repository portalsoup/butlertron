package com.portalsoup.mrbutlertron.commands


import com.portalsoup.discordbot.core.command.GuildJoinCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.discordbot.core.command.sendMessage
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.guild.GuildJoinEvent

class GreetNewMembers : GuildJoinCommand<GuildJoinEvent>(

    command {

        name { "Greetings" }
        description { "I greet new members automatically" }

        preconditions {
            predicate { true }
        }

        job {
            addRunner {
                val members = it.guild.members
                members.sortWith<Member>(Comparator { o1, o2 -> o1.timeJoined.compareTo(o2.timeJoined) }) // TODO this may be comparing backwards

                val memberName = members.last().effectiveName

                val channel = it.guild.getTextChannelsByName("bot-testing-ground", false).firstOrNull() ?: return@addRunner

                channel.sendMessage("Hello, ${memberName}!").queue()
            }

        }

    }
)