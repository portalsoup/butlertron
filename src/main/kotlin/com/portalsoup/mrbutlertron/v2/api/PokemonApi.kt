package com.portalsoup.mrbutlertron.v2.api

import com.portalsoup.mrbutlertron.v2.core.api.Api
import com.portalsoup.mrbutlertron.v2.dto.pokemon.Ability
import com.portalsoup.mrbutlertron.v2.dto.pokemon.Pokemon
import com.portalsoup.mrbutlertron.v2.dto.pokemon.PokemonHandle
import com.portalsoup.mrbutlertron.v2.dto.pokemon.PokemonSpecies
import kotlinx.serialization.decodeFromString
import org.json.JSONObject
import kotlinx.serialization.json.Json
import java.text.Normalizer

object PokemonApi {

    private val host = "https://pokeapi.co/api/v2/"

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    private fun Api.pokemonEndpoint(arg: Any): Pokemon {
        val response = makeRequest("${host}/pokemon/$arg")
        val pokemon = json.decodeFromString<Pokemon>(response)
        return pokemon.copy(
            wildHoldItems = pokemon.wildHoldItems.map {
                it.copy(
                    item = it.item,
                    frequency = listOf(it.frequency.last())
                )
            }
        )
    }

    private fun Api.pokemonSpeciesEndpoint(arg: Any): PokemonSpecies {
        val response = makeRequest("${host}/pokemon-species/$arg")
        val species = json.decodeFromString<PokemonSpecies>(response)
        return species.copy(
            flavorTexts = listOf(species.flavorTexts
                .last { it.language.name == "en" }
                .let { it.copy(text = it.text.replace("[^\\x00-\\x7F]".toRegex(), "")) }
            )
        )
    }

    private fun Api.pokemonAbilityEndpoint(url: String): Ability {
        val response = makeRequest(url)
        val ability = json.decodeFromString<Ability>(response)
        return ability.copy(
            effectEntries = listOf(
                ability.effectEntries.last { it.language.name == "en" }
            )
        )
    }

    fun getPokemon(name: String): PokemonHandle {
        val pokemon = Api.pokemonEndpoint(name)
        val pokemonSpecies = Api.pokemonSpeciesEndpoint(name)

        return PokemonHandle(
            pokemon.copy(abilities = pokemon.abilities.map {
                it.copy(shortText = Api.pokemonAbilityEndpoint(it.name.url)
                    .effectEntries
                    .last { it.language.name == "en" }
                    .shortEffect
                )
            }),
            pokemonSpecies
        )
    }
}