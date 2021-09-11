package com.portalsoup.mrbutlertron.v2.api

import net.dv8tion.jda.api.entities.MessageEmbed

interface Embeddable {
    suspend fun embed(term: String): MessageEmbed
}