package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.discordbot.core.command.type.sendMessage
import com.portalsoup.discordbot.core.extensions.Api
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.RuntimeException

class YugiohCardLookup : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    sendMessage {
        name { "Yugioh Card Lookup" }
        description { "ygo {card-name}" }

        preconditions {
            message {
                beginsWith { "ygo" }
            }
        }

        job {
            reply {
                val url = "https://db.ygoprodeck.com/api/v5/cardinfo.php?fname="

                try {
                    val term = it.message.contentRaw
                        .toLowerCase()
                        .substring(3)
                        .trim()
                        .replace(" ", "%20")
                    val response = Api.makeRequest(url + term)
                    try {
                        val outerArray = JSONArray(response)
                        val firstMatch = outerArray.getJSONObject(0)
                        val imagesArray = firstMatch.getJSONArray("card_images")
                        val firstImage = imagesArray.getJSONObject(0)
                        val imageUrl = firstImage.getString("image_url")
                        imageUrl
                    } catch (e: JSONException) {
                        "No match"
                    }
                } catch (e2: RuntimeException) {
                    it.channel.addReactionById(it.message.id, "❌")
                    "Oops, something went wrong"
                }
            }
        }
    }
)