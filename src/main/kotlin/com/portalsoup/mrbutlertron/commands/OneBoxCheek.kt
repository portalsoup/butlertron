package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class OneBoxCheek : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    command {
        preconditions {
            predicate {
                it.message.contentRaw
                    .trim()
                    .toLowerCase()
                    .equals("what'll i get for one box?")
            }
        }

        job {
            run {
                it.channel
                    .sendMessage("One *CHEEK!*")
                    .queue()
            }
        }
    }
)
)