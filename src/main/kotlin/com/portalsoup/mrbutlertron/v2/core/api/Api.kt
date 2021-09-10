package com.portalsoup.mrbutlertron.v2.core.api

import com.portalsoup.mrbutlertron.v2.core.getLogger
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.RuntimeException

object Api {

    private val log = getLogger(javaClass)

    fun makeRequest(url: String, headers: Map<String, String> = mapOf()): String {
        log.debug("Making query: $url")
        val apiClient = OkHttpClient()

        try {
            val request = Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build()

            val get = ""
            var done = false
            var response = ""

            apiClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    done = true
                }

                override fun onResponse(call: Call, r: Response) {
                    response = r.body()!!.string()
                    done = true
                }
            })

            while (!done) {
                Thread.sleep(500)
            }

            if (response.isEmpty() || response == "null") {
                log.info("Api failed: response=${response}")
                throw NoResultsFoundException()
            }

            log.debug("Returning response:\n\n$response\n\n")
            return response
        } catch (e: RuntimeException) {
            log.error("An API request has failed ", e.message)
            e.printStackTrace()
            throw e
        }
    }
    class NoResultsFoundException: RuntimeException()
}


fun JSONObject.safeGetString(str: String): String? {
    return if (has(str)) {
        getString(str)
    } else {
        null
    }
}

fun JSONObject.safeGetInt(int: String): Int? {
    return if (has(int)) {
        return try {
            getString(int).toInt()
        } catch (e: RuntimeException) {
            null
        }
    } else {
        null
    }
}