package com.portalsoup.mrbutlertron.commands.dsl.commands

import com.portalsoup.mrbutlertron.commands.dsl.GuildMessageReceivedCommand
import com.portalsoup.mrbutlertron.commands.dsl.command
import com.portalsoup.mrbutlertron.core.extensions.Api
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.json.JSONArray
import org.json.JSONException
import java.lang.RuntimeException

class YugiohCardLookup : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    command {

        name { "Yugioh Card Lookup" }

        description { "`ygo {card-name}`" }

        preconditions {
            predicate {
                it.message.contentRaw
                    .toLowerCase()
                    .trim()
                    .startsWith("ygo")
            }
        }

        job {
            run {
                println("Raw message=${it.message.contentRaw}")
                val url = "https://db.ygoprodeck.com/api/v5/cardinfo.php?fname="

                try {
                    val term = it.message.contentRaw
                        .toLowerCase()
                        .substring(3)
                        .trim()
                        .replace(" ", "%20")
                    val response = Api.makeRequest(url + term)
                    val imageUrl = try {
                        val outerArray = JSONArray(response)
                        println("Is an array")
                        val firstMatch = outerArray.getJSONObject(0)
                        println("first match=$firstMatch")
                        val imagesArray = firstMatch.getJSONArray("card_images")
                        println("images array size ${imagesArray.length()}")
                        val firstImage = imagesArray.getJSONObject(0)
                        println("first image found")
                        val imageUrl = firstImage.getString("image_url")
                        println("imageUrl=${imageUrl}")
                        imageUrl
                    } catch (e: JSONException) {
                        "No match"
                    }
                    it.channel.sendMessage(imageUrl).queue()
                } catch (e2: RuntimeException) {
                    it.channel.addReactionById(it.message.id, "❌")
                }
            }
        }
    }
)