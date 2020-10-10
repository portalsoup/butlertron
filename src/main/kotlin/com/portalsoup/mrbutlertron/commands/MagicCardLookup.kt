package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.discordbot.core.command.type.sendMessage
import com.portalsoup.discordbot.core.extensions.Api
import com.portalsoup.mrbutlertron.Environment
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.json.JSONObject
import java.lang.RuntimeException

class MagicCardLookup : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    sendMessage {
        name { "Magic: The Gathering card lookup" }
        description { "mtg {card-name}" }

        preconditions {
            message {
                beginsWith { "mtg" }
            }
        }

        job {
            reply {
                val url = "https://api.scryfall.com/cards/named?fuzzy="

                val noResultsText = "Didn't find the card.  The search could have been too broad to confidently" +
                        " pick the correct match, or didn't match any cards at all."

                val errorText = "Oops, something went wrong"

                try {
                    val term = it.message.contentRaw
                        .toLowerCase()
                        .replace("mtg", "")
                        .trim()
                        .replace(" ", "+")

                    val json = JSONObject(Api.makeRequest(url + term))
                    when {
                        json.has("image_uris") -> {
                            json
                                .getJSONObject("image_uris")
                                .getString("normal").toString()
                        }
                        json.has("details") -> {
                            json
                                .getString("details")
                        }
                        else -> {
                            throw RuntimeException()
                        }
                    }
                } catch (e: Api.NoResultsFoundException) {
                    noResultsText
                } catch (e2: RuntimeException) {
                    it.channel.addReactionById(it.message.id, "❌")
                    errorText
                }
            }
        }
    }
)