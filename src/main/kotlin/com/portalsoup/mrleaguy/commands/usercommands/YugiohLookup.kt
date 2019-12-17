package com.portalsoup.mrleaguy.commands.usercommands

import com.portalsoup.mrleaguy.commands.ApiCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
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

        val json = makeRequest(url + fuzzyName)
        println("raw json $json")
        val extractedJson: String = when {
            json.has("card_images") -> {
                println("has card_images")
                json
                    .getJSONObject("0")
                    .getString("image_url").toString()
            }
            json.has("error") -> {
                println("has error")
                json
                    .getString("No match.")
            }
            else -> {
                throw RuntimeException()
            }
        }

        println("Extracted ${extractedJson}")
        return extractedJson
    }
}