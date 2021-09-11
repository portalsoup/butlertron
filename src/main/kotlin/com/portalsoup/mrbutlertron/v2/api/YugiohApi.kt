package com.portalsoup.mrbutlertron.v2.api

import com.portalsoup.mrbutlertron.v2.core.api.Api
import com.portalsoup.mrbutlertron.v2.core.getLogger
import com.portalsoup.mrbutlertron.v2.dsl.cardEmbed
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import net.dv8tion.jda.api.entities.MessageEmbed
import org.json.JSONException
import org.json.JSONObject
import java.lang.RuntimeException

class YugiohApi: CardApi() {
    val log = getLogger(javaClass)
    val url = "https://db.ygoprodeck.com/api/v7/cardinfo.php?fname="

    override suspend fun getCardImage(term: String): String {
        return try {
            val response = getRawCardAsync(term)
            try {
                getImageUriFromJson(response.await())
            } catch (e: JSONException) {
                "No match"
            }
        } catch (e2: RuntimeException) {
            log.debug("Generic yugioh error", e2)
            "Oops, something went wrong"
        }
    }

    override suspend fun getRawCardAsync(term: String): Deferred<JSONObject> = coroutineScope {
        async { JSONObject(Api.makeRequest(url + term)).also { println(it.toString()) }  }
    }

    override fun getImageUriFromJson(json: JSONObject): String {
        val firstMatch = json.getJSONArray("data")[0] as JSONObject
        val imagesArray = firstMatch.getJSONArray("card_images")
        val firstImage = imagesArray.getJSONObject(0)
        return firstImage.getString("image_url")
    }

    override suspend fun getCardEmbed(term: String): MessageEmbed {
        val card = getRawCardAsync(term).await().getJSONArray("data").getJSONObject(0)
        return cardEmbed {
            uri = kotlin.runCatching { card.getString("name") }.getOrNull() ?: ""
            cardName = kotlin.runCatching { card.getString("type") }.getOrNull() ?: ""
            spellType = kotlin.runCatching { card.getString("race") }.getOrNull() ?: ""
            costLabel = "Level"
            cost = kotlin.runCatching { card.getInt("level").toString() }.getOrNull() ?: ""
            oracleText = kotlin.runCatching { card.getString("desc") }.getOrNull() ?: ""
            artCrop = kotlin.runCatching { card.getJSONArray("card_images").getJSONObject(0).getString("image_url") }.getOrNull() ?: ""
            atkLabel = "ATK"
            atk = kotlin.runCatching { card.getInt("atk").toString() }.getOrNull() ?: ""
            defLabel = "DEF"
            def = kotlin.runCatching { card.getInt("def").toString() }.getOrNull() ?: ""
        }
    }

}