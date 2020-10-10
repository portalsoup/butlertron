package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.type.sendMessage
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.reflections.Reflections

class CommandList : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(
    sendMessage {
        description {
            "List all commands."
        }

        preconditions {
            message {
                matchesToLowercase { "!(mr(\\.)?(\\s+)?)?butler(tron)?\\s+list" }
            }
        }

        job {
            reply {
                val reflections = Reflections()
                val dslCommands: List<GuildMessageReceivedCommand<*>> =
                    reflections.getSubTypesOf(GuildMessageReceivedCommand::class.java)
                        .map { it.getConstructor().newInstance() }

                val stringBuilder: StringBuilder = StringBuilder("\nCommands:\n")

                dslCommands.map { command: GuildMessageReceivedCommand<*> ->
                    val name = command.command.name.ifEmpty { command::class.simpleName }
                    val description: String = command.command.description
                        .split(Regex("\\n"))
                        .map { "--- $it" }
                        .joinToString(separator = "\n")

                    "$name\n${description}"
                }.forEach { stringBuilder.append(it).append("\n\n") }

                stringBuilder.toString()
            }
        }
    }
)