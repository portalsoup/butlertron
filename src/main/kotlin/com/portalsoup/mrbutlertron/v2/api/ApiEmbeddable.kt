package com.portalsoup.mrbutlertron.v2.api

import net.dv8tion.jda.api.entities.MessageEmbed

interface ApiEmbeddable {
    suspend fun embed(term: String): MessageEmbed
}