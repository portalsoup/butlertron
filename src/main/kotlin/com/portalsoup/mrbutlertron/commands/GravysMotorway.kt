package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class GravysMotorway : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    command {
        preconditions {
            predicate {
                it.message.contentRaw
                    .trim()
                    .toLowerCase()
                    .contains("i've got a meal on the motorway to the floater-way. you say?")
            }
        }

        job {
            run {
                it.channel
                    .sendMessage("Don't rush the flush, Daddio! Listen to the ass! Pass the gas! Listen to the ass! Pass the gas!")
                    .queue()
            }
        }
    }
)