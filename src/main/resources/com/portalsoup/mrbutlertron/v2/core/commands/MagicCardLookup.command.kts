import com.portalsoup.mrbutlertron.v2.api.MagicApi
import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

command {
    name = "Magic: The Gathering card lookup"
    description = "Fetches an image from an online API to display"

    job {
        precondition { it.formattedMessage().startsWith("!mtg") }
        action { event ->
            event.formattedMessage()
                .replace("!mtg", "")
                .trim()
                .replace(" ", "+")
                .let { runBlocking { MagicApi().getCardEmbed(it) } }
                .let { event.reply(it) }
        }
    }
}