package com.portalsoup.discordbot.core.extensions

import okhttp3.*
import java.io.IOException
import java.lang.RuntimeException

object Api {

    fun makeRequest(url: String): String {
        val apiClient = OkHttpClient()

        try {
            val request = Request.Builder()
                .url(url)
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
                println("Api failed: response=${response}")
                throw NoResultsFoundException()
            }

            return response
        } catch (e: RuntimeException) {
            println("FAILED!" + e.message)
            e.printStackTrace()
            throw e
        }
    }

    class NoResultsFoundException: RuntimeException()

}