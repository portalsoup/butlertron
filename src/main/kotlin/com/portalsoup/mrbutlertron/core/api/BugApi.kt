package com.portalsoup.mrbutlertron.core.api

import com.portalsoup.mrbutlertron.Environment
import com.portalsoup.mrbutlertron.core.Try
import com.portalsoup.mrbutlertron.core.TryFailedException
import org.json.JSONArray
import org.json.JSONObject

data class BugDTO(
    val name: String?,
    val url: String?,
    val number: Int?,
    val imageUrl: String?,
    val time: String?,
    val location: String?,
    val rarity: String?,
    val totalCatch: String?,
    val sellNook: String?,
    val sellFlick: String?
) {
    companion object {
        fun parse(json: JSONObject): BugDTO {
            return BugDTO(
                name = json.safeGetString("name"),
                url = json.safeGetString("url"),
                number = json.safeGetInt("number"),
                imageUrl = json.safeGetString("image_url"),
                time = json.safeGetString("time"),
                location = json.safeGetString("location"),
                rarity = json.safeGetString("rarity"),
                totalCatch = json.safeGetString("total_catch"),
                sellNook = json.safeGetString("sell_nook"),
                sellFlick = json.safeGetString("sell_flick")
            )
        }
    }
}

object BugApi {
    val url = "https://api.nookipedia.com"

    fun lookupBug(name: String): Try<BugDTO> {
        val raw = Api.makeRequest("$url/nh/bugs/$name", mapOf("X-API-KEY" to Environment.nookpediaToken))
        val json = JSONArray(raw)

        return parseBugJson(json)
    }

    fun parseBugJson(json: JSONArray): Try<BugDTO> {
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
                Try.Success(BugDTO.parse(json.getJSONObject(0)))
            } else {
                Try.Failure(TryFailedException("I couldn't find that villager..."))
            }
        }
    }
}