package com.portalsoup.mrleaguy.commands

import okhttp3.*
import java.io.IOException
import java.lang.RuntimeException


abstract class ApiCommand : AbstractCommand() {

    private val apiClient = OkHttpClient()

    fun makeRequest(url: String): String {
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

            print("Waiting ")
            while (!done) {
                print(".")
                Thread.sleep(500)
            }
            println("")

            if (response.isEmpty() || response == "null") {
                println("Failed: response=${response}")
                throw NoResultsFoundException()
            }

            println("Returning")
            return response
        } catch (e: RuntimeException) {
            println("FAILED!" + e.message)
            e.printStackTrace()
            throw e
        }
    }
    protected class NoResultsFoundException: RuntimeException()

}