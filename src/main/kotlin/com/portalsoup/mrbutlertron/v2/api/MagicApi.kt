package com.portalsoup.mrbutlertron.v2.api

import com.portalsoup.mrbutlertron.v2.core.api.Api
import org.json.JSONObject
import java.lang.RuntimeException

class MagicApi: CardApi() {

    val url = "https://api.scryfall.com/cards/named?fuzzy="
    val errorText = "Oops, something went wrong"

    val noResultsText = "Didn't find the card.  The search could have been too broad to confidently" +
            " pick the correct match, or didn't match any cards at all."

    override fun getRawCard(term: String): JSONObject =
        JSONObject(Api.makeRequest(url + term))

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

    override fun getCardImage(term: String): String {
        val result = kotlin.runCatching {
            val json = getRawCard(term)
            getImageUriFromJson(json)
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