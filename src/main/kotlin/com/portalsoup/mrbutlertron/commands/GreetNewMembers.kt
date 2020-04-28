package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildJoinCommand
import com.portalsoup.discordbot.core.command.type.onJoin
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import java.lang.RuntimeException

class GreetNewMembers : GuildJoinCommand<GuildMemberJoinEvent>(

    onJoin {

        name { "Greetings" }
        description { "I greet new members automatically" }

        preconditions {
            isABot { false }
        }

        job {
            addRunner {
                val channel = it.guild.defaultChannel ?: throw RuntimeException("No default channel found for ${it.guild.name}")
                    channel.sendMessage("Hello, ${it.member.effectiveName}!").queue()
            }
        }
    }
)