package com.portalsoup.mrbutlertron.v2.api

import com.portalsoup.mrbutlertron.v2.core.api.Api
import com.portalsoup.mrbutlertron.v2.dsl.cardEmbed
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import net.dv8tion.jda.api.entities.MessageEmbed
import org.json.JSONObject
import java.lang.RuntimeException

class MagicApi: CardApi() {

    val url = "https://api.scryfall.com/cards/named?fuzzy="
    val errorText = "Oops, something went wrong"

    val noResultsText = "Didn't find the card.  The search could have been too broad to confidently" +
            " pick the correct match, or didn't match any cards at all."

    override suspend fun getRawCardAsync(term: String): Deferred<JSONObject> = coroutineScope {
        async { JSONObject(Api.makeRequest(url + term)).also { println(it) } }
    }

    override fun getImageUriFromJson(json: JSONObject): String =
        when {
            json.has("image_uris") -> {
                json
                    .getJSONObject("image_uris")
                    .getString("normal").toString()
            }
            json.has("details") -> {
                json
                    .getString("details")
            }
            else -> {
                throw RuntimeException()
            }
        }

    override suspend fun embed(term: String): MessageEmbed {
        val card = getRawCardAsync(term).await()
        return cardEmbed {
            uri = kotlin.runCatching { card.getString("scryfall_uri") }.getOrNull() ?: ""
            cardName = kotlin.runCatching { card.getString("name") }.getOrNull() ?: ""
            costLabel = "Mana Cost"
            cost = kotlin.runCatching { card.getString("mana_cost") }.getOrNull() ?: ""
            oracleText = kotlin.runCatching { card.getString("oracle_text") }.getOrNull() ?: ""
            flavorText = kotlin.runCatching { card.getString("flavor_text") }.getOrNull() ?: ""
            cardImage = kotlin.runCatching { card.getJSONObject("image_uris").getString("large") }.getOrNull() ?: ""
            artCrop = kotlin.runCatching { card.getJSONObject("image_uris").getString("art_crop") }.getOrNull() ?: ""
            atkLabel = "Power"
            atk = kotlin.runCatching { card.getString("power") }.getOrNull() ?: ""
            defLabel = "Toughness"
            def = kotlin.runCatching { card.getString("toughness") }.getOrNull() ?: ""
            spellType = kotlin.runCatching { card.getString("type_line") }.getOrNull() ?: ""
        }
    }


    override suspend fun getCardImage(term: String): String {
        val result = kotlin.runCatching {
            val json = getRawCardAsync(term)
            getImageUriFromJson(json.await())
        }

        return if (result.isSuccess) {
            return result.getOrThrow()
        } else {
            when (result.exceptionOrNull()) {
                is Api.NoResultsFoundException -> noResultsText
                else -> errorText
            }
        }
    }
}