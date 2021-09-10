import com.portalsoup.mrbutlertron.v2.api.MagicApi
import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import kotlinx.coroutines.runBlocking

command {
    name = "Magic: The Gathering card lookup"
    description = "Fetches an image from an online API to display"

    job {
        precondition { it.formattedMessage().startsWith("!mtg") }
        action { event ->
            val jpg = event.formattedMessage()
                .replace("!mtg", "")
                .trim()
                .replace(" ", "+")
                .let { runBlocking { MagicApi().getCardImage(it) } }

            event.reply(jpg)
        }
    }
}