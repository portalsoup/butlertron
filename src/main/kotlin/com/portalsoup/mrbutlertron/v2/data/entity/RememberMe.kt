package com.portalsoup.mrbutlertron.v2.data.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object RememberMe: IntIdTable() {
    val discordUserId = varchar("discorduser", 255).uniqueIndex()
    val name = varchar("name", 255)
}