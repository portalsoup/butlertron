import com.portalsoup.mrbutlertron.v2.api.MagicApi
import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import kotlinx.coroutines.runBlocking

command {
    name = "Magic: The Gathering card lookup"
    description = "Fetches an image from an online API to display"
    command = "mtg"

    help {
        description = "Look up Magic: The Gathering cards from https://scryfall.com"
        trigger = "!mtg <card-name>"
        example { "!mtg infinity elemental" }
    }

    job("Lookup a card") {

        precondition { event ->
            event.checkMessagePredicate { it.isNotEmpty() }
        }

        action { event ->
            event.stripMessage()!!
                .trim()
                .replace(" ", "+")
                .let { runBlocking { MagicApi().embed(it) } }
                .let { event.reply(it) }
        }
    }

    // create job to get price info of card
}