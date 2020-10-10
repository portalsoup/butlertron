package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.discordbot.core.command.type.sendMessage
import com.portalsoup.mrbutlertron.Environment
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class SourceCodeLocation : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(
    sendMessage {
        description {
            "Ask Mr butlertron where his source code is"
        }

        preconditions {
            message {
                matchesToLowercase { ".*mr.*butlertron.*source.*code.*" }
            }
        }

        job {
            reply {
                Environment.githubUrl ?: "I don't know"
            }
        }
    }
)