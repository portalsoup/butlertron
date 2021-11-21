import com.portalsoup.mrbutlertron.v2.api.YugiohApi
import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import kotlinx.coroutines.runBlocking

val ygo = command {
    name = "Yugioh card lookup"
    command = "ygo"
    description = "Fetches an image from an online API to display"

    job("Yugioh card lookup") {
        help {
            description = ""
            trigger = ""
            example { "" }
        }

        precondition { event ->
            event.checkMessagePredicate { it.isNotEmpty() }
        }
        action { event ->
            val jpg = event.formattedMessage()
                .replace("!ygo", "")
                .trim()
                .replace(" ", "%20")
                .let { runBlocking { YugiohApi().embed(it) } }

            event.reply(jpg)
        }
    }
}