package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.discordbot.core.extensions.Api
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.json.JSONObject
import java.lang.RuntimeException

class NorrisJokes : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    command {

        description { "Say Chuck Norris's name" }

        preconditions {
            predicate {
                it.message.contentRaw
                    .toLowerCase()
                    .trim()
                    .matches(Regex(".*chuck*\\snorris.*"))
            }
        }

        job {
            addRunner {
                val jokeUrl = "http://api.icndb.com/jokes/random?exclude=[explicit]"
                val response = JSONObject(Api.makeRequest(jokeUrl))
                val joke: String = when {
                    response.has("value") && response["value"] is JSONObject -> {
                        response.getJSONObject("value").getString("joke")
                    }
                    else -> throw RuntimeException()
                }
                it.channel.sendMessage(joke).queue()
            }
        }
    }
)