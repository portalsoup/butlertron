import com.portalsoup.mrbutlertron.v2.core.Try
import com.portalsoup.mrbutlertron.v2.core.TryFailedException
import com.portalsoup.mrbutlertron.v2.dto.animalcrossing.Personality
import com.portalsoup.mrbutlertron.v2.dto.animalcrossing.Species
import com.portalsoup.mrbutlertron.v2.core.api.VillagerApi
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.MessageEmbed

fun getVillager(name: String? = null, species: String? = null, personality: String? = null): Try<MessageEmbed> {
    return runBlocking {
        VillagerApi.embed(
            name = name,
            species = species?.let { s -> Species.valueOf(s.toLowerCase().capitalize()) },
            personality = personality?.let { p -> Personality.valueOf(p.toLowerCase().capitalize()) }
        )
    }
}

command {
    name = "Animal crossing"
    command = "ac"
    description = "Lookup animal crossing resources"
    help {
        description = "Lookup things from animal crossing.  Villagers only for now."
        trigger = "${prefix}ac"
        example { "${prefix}ac villager -name=bam" }
        example { "${prefix}ac villager -species=deer" }
        example { "${prefix}ac villager -personality=jock" }
    }

    job("Villager lookup") {
        help {
            description = "Lookup villager from animal crossing."
            trigger = "${prefix}ac villager"
            example { "${prefix}ac villager -name=bam" }
            example { "${prefix}ac villager -species=deer" }
            example { "${prefix}ac villager -personality=jock" }
            imageUrl { "https://static.wikia.nocookie.net/logopedia/images/d/d2/Animal_Crossing_logo.png" }
        }

        precondition { event ->
            event.checkMessagePredicate {
                println("OK predicate run $it")
                it == "villager"
            }
        }
        action { event ->
            val message = event.message.contentRaw

            val findArgs = Regex("-(\\S+)=").findAll(message)
                .map { arg -> arg.groupValues[1] }

            val args = findArgs
                .map { Pair(it, Regex("-$it=(\\S+)").find(message)?.groupValues?.get(1)) }
                .toMap()
            val name = message.split(" ")
                .takeIf { it.size >= 2 }
                ?.get(1)

            println(args)

            val maybeVillager = if (args.values.isEmpty() && name != null) {
                getVillager(name = name)
            } else if (args.values.isNotEmpty()) {
                getVillager(
                    name = args["name"],
                    species = args["species"],
                    personality = args["personality"]
                )
            } else {
                event.reply(
                    """
                        >Provide one or more filters to find a villager
                        >Examples:
                        >    -name=bam
                        >    -species=deer
                        >    -personality=jock
                        >
                        >Example command:
                        >    !villager -name=bam -species=deer
                    """.trimMargin(">")
                )
                return@action
            }

            when (maybeVillager) {
                is Try.Success -> {
                    maybeVillager.data
                        .let { event.channel.sendMessage(it).queue() }
                }
                is Try.Failure -> {
                    println(maybeVillager)
                    when (maybeVillager.error) {
                        is TryFailedException -> {
                            println("about to reply failure")
                            val raw =
                                maybeVillager.error.message ?: throw RuntimeException("Unknown error")
                            if (raw.length > 1999) {
                                event.channel.sendMessage(raw.substring(0, 1998)).queue()
                            } else {
                                event.channel.sendMessage(raw).queue()
                            }
                        }
                    }
                }
            }
        }
    }
}