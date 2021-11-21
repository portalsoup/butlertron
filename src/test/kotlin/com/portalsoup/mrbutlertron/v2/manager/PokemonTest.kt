package com.portalsoup.mrbutlertron.v2.manager;

import com.portalsoup.mrbutlertron.v2.api.PokemonApi
import com.portalsoup.mrbutlertron.v2.dto.pokemon.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.testng.annotations.Test;

class PokemonTest {


    @Test
    fun testGetPokemon() {
        val actualPokemon = PokemonApi.getPokemon("pikachu")

        assertThat(actualPokemon, equalTo(TestData.pikachu))

        // computed fields
        assertThat(actualPokemon.pokemonSpecies.stepsToHatch, equalTo(2560))
        assertThat(actualPokemon.pokemonSpecies.femaleChance, equalTo(50))
    }
}

object TestData {
    val pikachu: PokemonHandle = PokemonHandle(
        pokemon = Pokemon(
            25,
            "pikachu",
            4,
            60,
            sprites = Sprites(
                front = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
                back = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/25.png",
                shinyFront = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/25.png",
                shinyBack = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/25.png",
                other = Sprites.Other(
                    home = Sprites.Home(
                        front = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/25.png",
                        shinyFront = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/25.png"
                    )
                )
            ),
            types = listOf(Type.create("electric")),
            stats = listOf(
                Stat.create(
                    base = 35,
                    effort = 0,
                    statName = "hp"
                ),
                Stat.create(
                    base = 55,
                    effort = 0,
                    statName = "attack"
                ),
                Stat.create(
                    base = 40,
                    effort = 0,
                    statName = "defense"
                ),
                Stat.create(
                    base = 50,
                    effort = 0,
                    statName = "special-attack"
                ),
                Stat.create(
                    base = 50,
                    effort = 0,
                    statName = "special-defense"
                ),
                Stat.create(
                    base = 90,
                    effort = 2,
                    statName = "speed"
                )
            ),
            abilities = listOf(
                PokemonAbility.create(
                    hidden = false,
                    name = "static",
                    url = "https://pokeapi.co/api/v2/ability/9/",
                    shortText = "Has a 30% chance of paralyzing attacking Pokémon on contact."
                ),
                PokemonAbility.create(
                    hidden = true,
                    name = "lightning-rod",
                    url = "https://pokeapi.co/api/v2/ability/31/",
                    shortText = "Redirects single-target electric moves to this Pokémon where possible.  Absorbs Electric moves, raising Special Attack one stage."
                ),
            ),
            wildHoldItems = listOf(
                WildHoldItem(
                    WildHoldItem.Item("oran-berry"), frequency = listOf(
                        WildHoldItem.ItemFrequency(50)
                    )
                ),
                WildHoldItem(
                    WildHoldItem.Item("light-ball"), frequency = listOf(
                        WildHoldItem.ItemFrequency(rarity = 5),
                    )
                )
            )
        ),
        pokemonSpecies = PokemonSpecies(
            pokedexId = 25,
            name = "pikachu",
            eggGroups = listOf(
                EggGroup(name = "ground"),
                EggGroup(name = "fairy")
            ),
            flavorTexts = listOf(
                FlavorText(
                    text = "When it smashes its opponents with its bolt-\n" +
                            "shaped tail, it delivers a surge of electricity\n" +
                            "equivalent to a lightning strike.",
                    language = FlavorText.FlavorTextLanguage(name = "en")
                )
            ),
            hatchCounter = 10,
            genderRate = 4
        )
    )
}
