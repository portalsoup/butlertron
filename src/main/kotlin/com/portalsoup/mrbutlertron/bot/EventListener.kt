package com.portalsoup.mrbutlertron.bot

import com.portalsoup.discordbot.core.command.GuildJoinCommand
import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.Job
import com.portalsoup.discordbot.core.command.PrivateMessageReceivedCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.reflections.Reflections
@Suppress("UNCHECKED_CAST")
class EventListener(private val bot: DiscordBot) : ListenerAdapter() {

    val history: Array<String> = arrayOf("","","","","","","","","","")
    var currentIndex = 0

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (history.contains(event.message.id)) {
            println("Found a duplicate!  ${event.message.id}")
            return
        } else {
            println("Adding ${event.message.id} to index=${currentIndex}")
            history[currentIndex] = event.message.id
            incrementHistoryCounter()
        }

        val guild = event.guild
        val channel = event.channel
        val permissionToPost = guild.selfMember.hasPermission(channel, Permission.MESSAGE_WRITE)

        if (event.message.author.isBot || !permissionToPost) {
            return
        }

        // dsl commands
        val reflections = Reflections()

        val dslCommands: List<GuildMessageReceivedCommand<*>> =
            reflections.getSubTypesOf(GuildMessageReceivedCommand::class.java)
                .map { it.getConstructor().newInstance() }

        dslCommands.filter {

                val predicates = it.command.preconditions as List<(GuildMessageReceivedEvent) -> Boolean>

            predicates
                .map { predicate -> predicate(event) }
                .fold(true) {total, next -> total && next}
        }.map { it.command.job as Job<GuildMessageReceivedEvent> }
            .flatMap { it.run }
            .forEach { it(event) }
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        // dsl commands
        val reflections = Reflections()

        val dslCommands: List<PrivateMessageReceivedCommand<*>> =
            reflections.getSubTypesOf(PrivateMessageReceivedCommand::class.java)
                .map { it.getConstructor().newInstance() }

        dslCommands.filter {

            val predicates = it.command.preconditions as List<(PrivateMessageReceivedEvent) -> Boolean>

            predicates
                .map { predicate -> predicate(event) }
                .fold(true) {total, next -> total && next}
        }.map { it.command.job as Job<PrivateMessageReceivedEvent> }
            .flatMap { it.run }
            .forEach { it(event) }
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        // dsl commands
        val reflections = Reflections()

        val dslCommands: List<GuildJoinCommand<*>> =
            reflections.getSubTypesOf(GuildJoinCommand::class.java)
                .map { it.getConstructor().newInstance() }

        dslCommands.filter {

            val predicates = it.command.preconditions as List<(GuildMemberJoinEvent) -> Boolean>

            predicates
                .map { predicate -> predicate(event) }
                .fold(true) {total, next -> total && next}
        }.map { it.command.job as Job<GuildMemberJoinEvent> }
            .flatMap { it.run }
            .forEach { it(event) }
    }

    fun incrementHistoryCounter() {
        if (currentIndex >= history.size - 1) {
            currentIndex = 0
        } else {
            currentIndex++
        }
    }
}