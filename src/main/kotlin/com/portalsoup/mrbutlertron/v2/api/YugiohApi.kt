package com.portalsoup.mrbutlertron.v2.api

import com.portalsoup.mrbutlertron.v2.core.api.Api
import com.portalsoup.mrbutlertron.v2.core.getLogger
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.RuntimeException

abstract class CardApi {
    abstract fun getCardImage(term: String): String
    internal abstract fun getRawCard(term: String): JSONObject
    internal abstract fun getImageUriFromJson(json: JSONObject): String
}

class YugiohApi: CardApi() {
    val log = getLogger(javaClass)
    val url = "https://db.ygoprodeck.com/api/v7/cardinfo.php?fname="

    override fun getCardImage(term: String): String {
        return try {
            val response = getRawCard(term)
            try {
                getImageUriFromJson(response)
            } catch (e: JSONException) {
                "No match"
            }
        } catch (e2: RuntimeException) {
            log.debug("Generic yugioh error", e2)
            "Oops, something went wrong"
        }
    }

    override fun getRawCard(term: String): JSONObject =
        JSONObject(Api.makeRequest(url + term))

    override fun getImageUriFromJson(json: JSONObject): String {
        val firstMatch = json.getJSONArray("data")[0] as JSONObject
        val imagesArray = firstMatch.getJSONArray("card_images")
        val firstImage = imagesArray.getJSONObject(0)
        return firstImage.getString("image_url")
    }
}