package com.portalsoup.mrbutlertron.commands.messagereceived.usercommands

import com.portalsoup.mrbutlertron.commands.messagereceived.GuildMessageReceivedCommand
import com.portalsoup.mrbutlertron.core.command.delegates.implementors.ApiRequester
import com.portalsoup.mrbutlertron.core.command.delegates.types.Requestable
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.json.JSONObject
import java.lang.RuntimeException

class ChuckNorrisJoke : GuildMessageReceivedCommand(), Requestable by ApiRequester() {
    init {
        addPrecondition { it.message.contentRaw.matches(Regex(".*chuck*\\snorris.*")) }
    }

    override fun run(event: GuildMessageReceivedEvent) {
        val url = "http://api.icndb.com/jokes/random?exclude=[explicit]"
        val response = JSONObject(makeRequest(url))
        val joke: String = when {
            response.has("value") && response["value"] is JSONObject -> {
                println("found the value")
                response.getJSONObject("value").getString("joke")
            }
            else -> throw RuntimeException()
        }
        event.channel.sendMessage(joke).queue()
    }

}