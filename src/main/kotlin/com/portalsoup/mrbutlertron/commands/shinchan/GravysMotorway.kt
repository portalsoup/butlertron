package com.portalsoup.mrbutlertron.commands.shinchan

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.discordbot.core.command.sendMessage
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class GravysMotorway : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    sendMessage {
        preconditions {
            predicate {
                it.message.contentRaw
                    .trim()
                    .toLowerCase()
                    .contains("i've got a meal on the motorway to the floater-way. you say?")
            }
        }

        job {
            reply { "Don't rush the flush, Daddio! Listen to the ass! Pass the gas! Listen to the ass! Pass the gas!" }
        }
    }
)