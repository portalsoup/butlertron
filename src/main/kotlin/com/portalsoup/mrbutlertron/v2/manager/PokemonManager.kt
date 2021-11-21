package com.portalsoup.mrbutlertron.v2.manager

import com.portalsoup.mrbutlertron.v2.api.PokemonApi
import com.portalsoup.mrbutlertron.v2.dsl.embed
import net.dv8tion.jda.api.entities.MessageEmbed

object PokemonManager {

    fun getPokemonByName(pkmnName: String, shiny: Boolean): MessageEmbed {
        val pokemonHandle = PokemonApi.getPokemon(pkmnName)
        val pokemon = pokemonHandle.pokemon
        val species = pokemonHandle.pokemonSpecies

        val abilitiesText = pokemon.abilities.map { ability ->
            "**${ability.name.name}**${ability.shortText?.let { " - $it" } ?: ""}"
        }

        val hp = pokemon.stats.first { it.stat.name == "hp" }.base
        val atk = pokemon.stats.first { it.stat.name == "attack" }.base
        val def = pokemon.stats.first { it.stat.name == "defense" }.base
        val spAtk = pokemon.stats.first { it.stat.name == "special-attack" }.base
        val spDef = pokemon.stats.first { it.stat.name == "special-defense" }.base
        val spd = pokemon.stats.first { it.stat.name == "speed" }.base

        val stats = "**HP**: $hp **Atk**: $atk **Sp Atk**: $spAtk" +
                  "\n**Spd**: $spd **Def**: $def  **Sp Def**: $spDef"

        val types = pokemon.types.map { type -> type.name.name }.joinToString(", ")

        val frontSprite = shiny
            .takeIf { it }
            ?.let { pokemon.sprites.shinyFront }
            ?: pokemon.sprites.front

        val backSprite = shiny
            .takeIf { it }
            ?.let { pokemon.sprites.shinyBack }
            ?: pokemon.sprites.back

        val frontHighQualitySprite = shiny
            .takeIf { it }
            ?.let { pokemon.sprites.other?.home?.shinyFront }
            ?: pokemon.sprites.other?.home?.front

        return embed {
            title = "${pokemon.name} (#${pokemon.pokedexId})"
            description = species.flavorTexts.first().text

            field {
                inline = true
                name = "Base stats"
                value = stats
            }

            field {
                inline = false
                name = "Types"
                value = types
            }

            field {
                inline = true
                name = "EV Points"
                value = pokemon.stats.filter { it.effort > 0 }.map { "${it.effort} ${it.stat.name}" }.joinToString("\n")
            }

            field {
                inline = true
                name = "Chance of female"
                value = "${species.femaleChance}%"
            }

            field {
                inline = true
                name = "Steps to hatch"
                value = species.stepsToHatch.toString()
            }

            field {
                inline = false
                name = "Abilities"
                value = abilitiesText.joinToString("\n\n")
            }

            image {
                url = frontHighQualitySprite ?: frontSprite
            }

            thumbnail {
                url = backSprite
            }
        }
    }
}