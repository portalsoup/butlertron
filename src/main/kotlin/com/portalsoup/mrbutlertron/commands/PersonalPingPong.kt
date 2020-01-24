package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.PrivateMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent

class PersonalPingPong : PrivateMessageReceivedCommand<PrivateMessageReceivedEvent>(

    command {
        description { "ping me in DM" }
        name { "`ping`-pong" }
        preconditions {
            predicate {
                it.message.contentRaw
                    .trim()
                    .toLowerCase()
                    .equals("ping")
            }
        }

        job {
            run { event ->
                event.message.author.openPrivateChannel().queue { channel ->
                    channel.sendMessage("pong").queue()
                }
            }
        }
    }
)