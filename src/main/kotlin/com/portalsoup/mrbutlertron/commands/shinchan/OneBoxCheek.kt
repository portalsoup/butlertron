package com.portalsoup.mrbutlertron.commands.shinchan

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.type.sendMessage
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class OneBoxCheek : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    sendMessage {
        preconditions {
            predicate {
                it.message.contentRaw
                    .trim()
                    .toLowerCase()
                    .equals("what'll i get for one box?")
            }
        }

        job {
            reply { "One *CHEEK!*" }
        }
    }
)