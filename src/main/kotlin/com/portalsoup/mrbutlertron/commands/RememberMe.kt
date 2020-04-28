package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.PrivateMessageReceivedCommand
import com.portalsoup.discordbot.core.command.type.sendDM
import com.portalsoup.mrbutlertron.data.entity.RememberMe
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class RememberMe : PrivateMessageReceivedCommand<PrivateMessageReceivedEvent>(
    sendDM {
        description { "I'll remember your nickname" }
        name { "" }

        preconditions {
            message {
                beginsWith { "call me" }
            }
        }

        job {
            addRunner { event ->
                val newName = event.message.contentRaw.replace("call me", "").trim()

                if (newName.isEmpty()) {

                    val name = transaction {
                        RememberMe
                            .select { RememberMe.discordUserId eq event.author.id }
                            .map { it[RememberMe.name] }
                            .single()
                    }

                    event.channel.sendMessage("Ah, yes. ${name}").queue()
                } else {
                    transaction {
                        val exists = RememberMe
                            .select { RememberMe.discordUserId eq event.author.id }
                            .count() > 0

                        if (exists) {
                            RememberMe.update({ RememberMe.discordUserId eq event.author.id}) {
                                it[name] = newName
                            }
                        } else {
                            RememberMe
                                .insert {
                                    it[discordUserId] = event.author.id
                                    it[name] = newName
                                }
                        }
                    }
                    event.channel.sendMessage("Ah, yes. Noted").queue()
                }
            }
        }
    }
)