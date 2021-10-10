import com.portalsoup.mrbutlertron.v2.core.api.Api
import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import org.json.JSONObject
import java.lang.RuntimeException

command {
    name = "Chuck norris jokes"
    description = "Share random chuck norris jokes"

    help {
        description = ""
        trigger = ""

        action(
            "",
            "",
            ""
        )
    }

    // TODO: This got into a loop because the joke output has "chuck norris" in it when the trigger phrase was the same.
    //  I think adding a sent message history table to prevent self replies that all messages check before
    //  running preconditions
    job {
        precondition { it.formattedMessage().matches(Regex("!norris")) }
        action {
            val jokeUrl = "http://api.icndb.com/jokes/random?exclude=[explicit]"
            val response = JSONObject(Api.makeRequest(jokeUrl))
            val joke: String = when {
                response.has("value") && response["value"] is JSONObject -> {
                    response.getJSONObject("value").getString("joke")
                }
                else -> throw RuntimeException()
            }
            it.reply(joke)
        }
    }
}