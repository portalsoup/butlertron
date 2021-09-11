package com.portalsoup.mrbutlertron.v2.core.api

import com.portalsoup.mrbutlertron.Environment
import com.portalsoup.mrbutlertron.v2.core.Try
import com.portalsoup.mrbutlertron.v2.core.TryFailedException
import com.portalsoup.mrbutlertron.v2.dsl.embed
import net.dv8tion.jda.api.entities.MessageEmbed
import org.json.JSONArray
import org.json.JSONObject

data class VillagerDTO(
    val name: String?,
    val url: String?,
    val altName: String?,
    val id: String?,
    val imageUrl: String?,
    val species: Species?,
    val personality: Personality?,
    val gender: String?,
    val birthdayMonth:  String?,
    val birthdayDay: Int?,
    val sign: String?,
    val quote: String?,
    val phrase: String?,
    val clothing: String?
) {
    companion object {
        fun parse(json: JSONObject): VillagerDTO {
            return VillagerDTO(
                name = json.safeGetString("name"),
                url = json.safeGetString("url"),
                altName = json.safeGetString("t_name"),
                id = json.safeGetString("id"),
                imageUrl = json.safeGetString("image_url"),
                species = json.safeGetString("species")?.let { Species.valueOf(it) },
                personality = json.safeGetString("personality")?.let { Personality.valueOf(it) },
                gender = json.safeGetString("gender"),
                birthdayMonth = json.safeGetString("birthday_month"),
                birthdayDay = json.safeGetInt("birthday_day"),
                sign = json.safeGetString("sign"),
                quote = json.safeGetString("quote"),
                phrase = json.safeGetString("phrase"),
                clothing = json.safeGetString("clothing")
            )
        }

    }
}

object VillagerApi {
    val url = "https://api.nookipedia.com/villagers"


    fun lookupVillager(
        name: String? = null,
        species: Species? = null,
        personality: Personality? = null,
        game: Game? = null,
        birthmonth: String? = null,
        birthday: Int? = null,
        nhdetails: Boolean? = null,
        excludeDetails: Boolean? = null,
        thumbsize: Int? = null
    ): Try<VillagerDTO> {
        val parameters = mutableListOf<String>()

        name?.also { parameters.add("name=$it") }
        species?.also { parameters.add("species=${it.name}") }
        personality?.also { parameters.add("personality=${it.name}") }
        game?.also { parameters.add("game=${it.name}") }
        birthmonth?.also { parameters.add("birthmonth=$it") }
        birthday?.also { parameters.add("birthday=$it") }
        nhdetails?.also { parameters.add("nhdetails=$it") }
        excludeDetails?.also { parameters.add("excludedetails=$it") }
        thumbsize?.also { parameters.add("thumbsize=$it") }

        val apiUrl = parameters
            .joinToString("&")
            .takeIf { it.isNotEmpty() }
            ?.let { "$url?$it" }
            ?: url

        println("final url $apiUrl")
        val raw = Api.makeRequest(apiUrl, mapOf("X-API-KEY" to Environment.nookpediaToken))
        val json = JSONArray(raw)

//        println(json)
        return parseVillagerJson(json)
    }

    fun parseVillagerJson(json: JSONArray): Try<VillagerDTO> {
        return if (json.length() > 1) {
            return Try.Failure.reason("Found multiple villagers that match that description.\n" +
                    json
                        .map { it as JSONObject }
                        .take(15)
                        .map {
                            "    * ${it.getString("name")} the ${it.getString("personality")} ${
                                it.getString(
                                    "species"
                                )
                            }"
                        }
                        .joinToString("\n") +
                    "\nRefine the search by adding filters such as \"!villagers -name=${
                        json.getJSONObject(1).getString("name")
                    }\"")
        } else {
            if (json.length() > 0) {
                Try.Success(VillagerDTO.parse(json.getJSONObject(0)))
            } else {
                Try.Failure(TryFailedException("I couldn't find that villager..."))
            }
        }
    }

    suspend fun embed(
        name: String?,
        species: Species?,
        personality: Personality?
    ): Try<MessageEmbed> {
        return when (val it = lookupVillager(name, species, personality)) {
            is Try.Failure -> it
            is Try.Success -> embed {
                val villager = it.data
                description = villager.url

                thumbnail {
                    url = ""
                }

                image {
                    url = villager.imageUrl
                }

                footer {
                    text = "${villager.phrase}"
                }

                villager.name
                    ?.also {
                        field {
                            this.name = "Name"
                            value = it
                            inline = true
                        }
                    }

                villager.gender
                    ?.also {
                        field {
                            this.name = "Gender"
                            value = it
                            inline = true
                        }
                    }
                (villager.birthdayDay != null || villager.birthdayMonth != null)
                    .takeIf { it }
                    ?.also {
                        field {
                            this.name = "Birthday"
                            value = listOf(villager.birthdayMonth, villager.birthdayDay.toString())
                                .joinToString(" ")
                            inline = true
                        }
                    }

                villager.quote
                    ?.also {
                        field {
                            value = it
                            inline = false
                        }
                    }

                villager.species?.name
                    ?.also {
                        field {
                            this.name = "Species"
                            value = it
                            inline = true
                        }
                    }

                villager.personality?.name
                    ?.also {
                        field {
                            this.name = "Personality"
                            value = it
                            inline = true
                        }
                    }

                villager.sign
                    ?.also {
                        field {
                            this.name = "Sign"
                            value = it
                            inline = true
                        }
                    }

                villager.clothing
                    ?.also {
                        field {
                            this.name = "Clothing"
                            value = it
                            inline = true
                        }
                    }


            }.let { Try.Success(it) }
        }
    }

}

enum class Species {
    Alligator,
    Anteater,
    Bear,
    `Bear cub`,
    Bird,
    Bull,
    Cat,
    Cub,
    Chicken,
    Cow,
    Deer,
    Dog,
    Duck,
    Eagle,
    Elephant,
    Frog,
    Goat,
    Gorilla,
    Hamster,
    Hippo,
    Horse,
    Koala,
    Kangaroo,
    Lion,
    Monkey,
    Mouse,
    Octopus,
    Ostrich,
    Penguin,
    Pig,
    Rabbit,
    Rhino,
    Sheep,
    Quirrel,
    Tiger,
    Wolf;
}

enum class Personality {
    Lazy,
    Jock,
    Cranky,
    Smug,
    Normal,
    Peppy,
    Snooty,
    Sisterly;
}

enum class Game {
    DNM,
    AC,
    E_PLUS,
    WW,
    CF,
    NL,
    WA,
    NH,
    FILM,
    HHD,
    PD;
}