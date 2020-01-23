package com.portalsoup.mrbutlertron.commands.dsl.commands

import com.portalsoup.mrbutlertron.commands.dsl.GuildJoinCommand
import com.portalsoup.mrbutlertron.commands.dsl.command
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
            run {
                val members = it.guild.members
                members.sortWith<Member>(Comparator { o1, o2 -> o1.timeJoined.compareTo(o2.timeJoined) }) // TODO this may be comparing backwards

                val memberName = members.last().effectiveName

                val channel = it.guild.getTextChannelsByName("bot-testing-ground", false).firstOrNull() ?: return@run

                channel.sendMessage("Hello, ${memberName}!").queue()
            }
        }

    }
)