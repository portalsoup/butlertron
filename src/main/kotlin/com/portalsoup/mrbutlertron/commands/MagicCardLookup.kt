package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.discordbot.core.extensions.Api
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.json.JSONObject
import java.lang.RuntimeException

class MagicCardLookup : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    command {

        name { "Magic: The Gathering card lookup" }
        description { "`mtg {card-name}`" }

        preconditions {
            predicate {
                it.message.contentRaw
                    .toLowerCase()
                    .trim()
                    .startsWith("mtg")
            }
        }

        job {
            run {
                val url = "https://api.scryfall.com/cards/named?fuzzy="

                val noResultsText = "Didn't find the card.  The search could have been too broad to confidently" +
                        " pick the correct match, or didn't match any cards at all."
                try {
                    val term = it.message.contentRaw
                        .toLowerCase()
                        .substring(3)
                        .trim()
                        .replace(" ", "+")

                    val json = JSONObject(Api.makeRequest(url + term))
                    val extractedJson: String = when {
                        json.has("image_uris") -> {
                            println("has image_uris")
                            json
                                .getJSONObject("image_uris")
                                .getString("normal").toString()
                        }
                        json.has("details") -> {
                            println("has details")
                            json
                                .getString("details")
                        }
                        else -> {
                            throw RuntimeException()
                        }
                    }

                    it.channel.sendMessage(extractedJson).queue()
                } catch (e: Api.NoResultsFoundException) {
                    it.channel.sendMessage(noResultsText).queue()
                } catch (e2: RuntimeException) {
                    it.channel.addReactionById(it.message.id, "❌")
                }
            }
        }
    }
)