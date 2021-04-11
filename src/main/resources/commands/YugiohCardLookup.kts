import com.portalsoup.mrbutlertron.v2.api.YugiohApi
import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command

command {
    name = "Yugioh card lookup"
    description = "Fetches an image from an online API to display"

    job {
        precondition { it.formattedMessage().startsWith("ygo") }
        action { event ->
            val jpg = event.formattedMessage()
                .replace("ygo", "")
                .trim()
                .replace(" ", "%20")
                .let { YugiohApi().getCardImage(it) }

            event.reply(jpg)
        }
    }
}