package com.portalsoup.mrleaguy.commands.usercommands

import com.portalsoup.mrleaguy.commands.ApiCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.json.JSONObject
import java.lang.RuntimeException

class MagicLookup : ApiCommand() {

    val url = "https://api.scryfall.com/cards/named?fuzzy="

    val noResultsText = "Didn't find the card.  The search could have been too broad to confidently" +
            " pick the correct match, or didn't match any cards at all."

    override fun syntaxDescription(): String =
        "mtg {card-name}"

    override fun runPredicate(event: GuildMessageReceivedEvent): Boolean {
        return prefixPredicate(event.message.contentRaw, "mtg")
    }

    override fun run(event: GuildMessageReceivedEvent) {
        println("Raw message=${event.message.contentRaw}")
        try {
            val term = event.message.contentRaw
                .toLowerCase()
                .substring(3)
                .trim()
                .replace(" ", "+")
            println("term: ${term}")

            val cardUrl = makeCardRequest(term)
            println("cardUrl: ${cardUrl}")

            event.channel.sendMessage(cardUrl).queue()
        } catch (e: NoResultsFoundException) {
            event.channel.sendMessage(noResultsText).queue()
        } catch (e2: RuntimeException) {
            event.channel.addReactionById(event.message.id, "❌")
        }
    }

    fun makeCardRequest(fuzzyName: String): String {
        val json = JSONObject(makeRequest(url + fuzzyName))
        println("raw json $json")
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

        println("Extracted ${extractedJson}")
        return extractedJson
    }
}
