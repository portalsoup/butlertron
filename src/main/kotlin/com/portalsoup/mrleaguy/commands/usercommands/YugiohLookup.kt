package com.portalsoup.mrleaguy.commands.usercommands

import com.portalsoup.mrleaguy.commands.ApiCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.RuntimeException

class YugiohLookup : ApiCommand() {

    private val url = "https://db.ygoprodeck.com/api/v5/cardinfo.php?fname="

    override fun runPredicate(event: GuildMessageReceivedEvent): Boolean {
        return prefixPredicate(event.message.contentRaw, "ygo")
    }

    override fun run(event: GuildMessageReceivedEvent) {
        println("Raw message=${event.message.contentRaw}")
        try {
            val term = event.message.contentRaw
                .toLowerCase()
                .substring(3)
                .trim()
                .replace(" ", "%20")
            println("term: ${term}")

            val cardUrl = makeCardRequest(term)
            println("cardUrl: ${cardUrl}")

            event.channel.sendMessage(cardUrl).queue()
        } catch (e2: RuntimeException) {
            event.channel.addReactionById(event.message.id, "❌")
        }
    }

    fun makeCardRequest(fuzzyName: String): String {

        val response = makeRequest(url + fuzzyName)
        println("raw response $response")

        val url = try {
            JSONArray(response)
                .getJSONObject(0)
                .getJSONObject("card_images")
                .getJSONObject("0")
                .getString("image_url")
        } catch (e: JSONException) {
            "No match"
        }

        println("Extracted ${url}")
        return url
    }
}