package com.portalsoup.mrbutlertron.core.command.delegates.types

interface Requestable {
    fun makeRequest(uri: String): String
}