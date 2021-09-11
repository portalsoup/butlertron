package com.portalsoup.mrbutlertron.v2.api

import com.portalsoup.mrbutlertron.v2.dsl.cardEmbed
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.MessageEmbed
import org.json.JSONObject

abstract class CardApi: Embeddable {
    abstract suspend fun getCardImage(term: String): String
    internal abstract suspend fun getRawCardAsync(term: String): Deferred<JSONObject>
    internal abstract fun getImageUriFromJson(json: JSONObject): String
}