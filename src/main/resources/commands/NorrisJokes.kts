import com.portalsoup.mrbutlertron.v2.core.api.Api
import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import org.json.JSONObject
import java.lang.RuntimeException

command {
    name = "Chuck norris jokes"
    description = "Share random chuck norris jokes"

    // TODO: This gets into a loop.  I think adding a message history table to prevent duplicate responses?
    job {
        precondition { it.formattedMessage().matches(Regex(".*chuck*\\snorris.*")) }
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