package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildJoinCommand
import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.discordbot.core.command.type.sendMessage
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import javax.annotation.RegEx

class CommandSandbox : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    sendMessage {
        preconditions {
            message {
                beginsWith { "!sandbox" }
            }
        }

        job {
            reply {
                "Wow!"
            }

            replyDM {
                "Wow!"
            }
        }
    }
)