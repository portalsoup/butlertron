import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import com.portalsoup.mrbutlertron.v2.manager.PokemonManager

val pokedex = command {
    name = "Pokedex"
    description = ""
    command = "pokedex"

    job("Get a pokemon") {
        help {
            description = ""
            trigger = ""
            example { "" }
        }

        action { event ->
            val isShiny = event.stripMessage()?.contains("shiny") ?: false
            event.stripMessage()!!
                .replace("shiny", "")
                .trim()
                .replace(" ", "+")
                .let { PokemonManager.getPokemonByName(it, isShiny) }
                .also { event.reply(it) }
        }
    }
}