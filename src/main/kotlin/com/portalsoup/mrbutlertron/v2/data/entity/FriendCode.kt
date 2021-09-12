package com.portalsoup.mrbutlertron.v2.data.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object FriendCode: IntIdTable(name = "FRIENDCODE") {
    val discordUserId = varchar("discorduser", 255)
    val ntdoCode = varchar("NTDOCODE", 255)
}