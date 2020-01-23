package com.portalsoup.mrbutlertron.commands.dsl.commands

import com.portalsoup.mrbutlertron.commands.dsl.PrivateMessageReceivedCommand
import com.portalsoup.mrbutlertron.commands.dsl.command
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent

class PersonalPingPong : PrivateMessageReceivedCommand<PrivateMessageReceivedEvent>(

    command {
        description { "ping me in DM" }
        name { "`ping`-pong" }
        preconditions {
            predicate {
                println("yes?")
                it.message.contentRaw
                    .trim()
                    .toLowerCase()
                    .equals("ping")
            }
        }

        job {
            run { event ->
                println("yes")
                println(event.message.author.toString())
                event.message.author.openPrivateChannel().queue { channel ->
                    channel.sendMessage("pong").queue()
                }
            }
        }
    }
)