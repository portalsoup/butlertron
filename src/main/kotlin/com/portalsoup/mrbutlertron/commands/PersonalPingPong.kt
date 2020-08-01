package com.portalsoup.mrbutlertron.commands


import com.portalsoup.discordbot.core.command.PrivateMessageReceivedCommand
import com.portalsoup.discordbot.core.command.type.sendDM
import com.portalsoup.discordbot.core.command.type.sendMessage
import com.portalsoup.mrbutlertron.core.getLogger
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent

class PersonalPingPong : PrivateMessageReceivedCommand<PrivateMessageReceivedEvent>(

    sendDM {
        description { "ping me in DM" }
        name { "`ping`-pong" }

        preconditions {
            getLogger(javaClass).info("Entered preconditions for ping pong")
            message {
                equalsIgnoreCase {
                    "ping"
                }
            }
        }


        job {

            reply { "Pong 1!" }

            reply { "Pong 2!" }
        }
    }
)