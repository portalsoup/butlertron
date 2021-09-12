package com.portalsoup.mrbutlertron.v2.manager

import com.portalsoup.mrbutlertron.v2.data.entity.FriendCode
import com.portalsoup.mrbutlertron.v2.manager.FriendCodeAction.*
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object FriendCodeManager {

    private val friendCodeRegex = Regex("(SW-\\d{4}-\\d{4}-\\d{4})")


    fun parseCommandMessage(rawDiscordMessage: MessageReceivedEvent): FriendCodeAction? {
        val rawMessage: String = rawDiscordMessage.message.contentRaw
        val sendingUser: User = rawDiscordMessage.message.author
        val userToFind: User = rawDiscordMessage.message.mentionedUsers.getOrNull(0) ?: sendingUser
        val code: String? = parseCode(rawMessage)

        val isAdd = "add".toRegex().containsMatchIn(rawMessage)
        val isRemove = "remove".toRegex().containsMatchIn(rawMessage)

        println("add=$isAdd remove=$isRemove code=$code toFind=$userToFind")
        return when {
            !isAdd && !isRemove -> Find(userToFind)
            isAdd && code != null -> Add(sendingUser, code)
            isRemove -> Remove(sendingUser)
            else -> null
        }
    }

    fun parseCode(rawDiscordMessage: String): String? = Regex("(SW-\\d{4}-\\d{4}-\\d{4})")
        .find(rawDiscordMessage)
        ?.also { println(it.value) }
        ?.value

}

sealed class FriendCodeAction {

    abstract fun run(): String?

    data class Find(val user: User) : FriendCodeAction() {
        override fun run(): String? = transaction {
            FriendCode
                .select { FriendCode.discordUserId eq user.id }
                .map { it[FriendCode.ntdoCode] }
                .singleOrNull()
        }
    }

    data class Add(val user: User, val code: String) : FriendCodeAction() {
        override fun run(): String = transaction {
            FriendCode
                .insert {
                    it[discordUserId] = user.id
                    it[ntdoCode] = code
                }

            "Code added!"
        }
    }

    data class Remove(val user: User) : FriendCodeAction() {
        override fun run(): String = transaction {
            FriendCode
                .deleteWhere { FriendCode.discordUserId eq user.id }

            "Code deleted"
        }
    }
}