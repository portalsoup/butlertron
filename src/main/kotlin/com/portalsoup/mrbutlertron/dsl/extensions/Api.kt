package com.portalsoup.discordbot.core.extensions

import com.portalsoup.mrbutlertron.core.getLogger
import okhttp3.*
import java.io.IOException
import java.lang.RuntimeException

object Api {

    private val log = getLogger(javaClass)

    fun makeRequest(url: String, headers: Map<String, String> = mapOf()): String {
        val apiClient = OkHttpClient()

        try {
            val request = Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build()

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

            return response
        } catch (e: RuntimeException) {
            log.error("An API request has failed ", e.message)
            e.printStackTrace()
            throw e
        }
    }

    class NoResultsFoundException: RuntimeException()

}