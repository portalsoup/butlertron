package com.portalsoup.mrbutlertron.bot

import com.portalsoup.discordbot.core.command.GuildJoinCommand
import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.Job
import com.portalsoup.discordbot.core.command.PrivateMessageReceivedCommand
import com.portalsoup.mrbutlertron.core.getLogger
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.reflections.Reflections
@Suppress("UNCHECKED_CAST")
class EventListener(private val bot: DiscordBot) : ListenerAdapter() {

    private val log = getLogger(javaClass)

    // TODO This needs to be replaced with a database table
    val history: Array<String> = arrayOf("","","","","","","","","","")
    var currentIndex = 0

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        log.info("Received a message from a guild server.")
        val messageId = event.message.id

        if (history.contains(messageId)) {
            log.debug("Encountered an event with a duplicate message ID [$messageId]")
            return
        } else {
            log.debug("Adding $messageId to index=$currentIndex")
            history[currentIndex] = messageId
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
        log.info("Received a private message from a user.")
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
        log.info("Detected that a new user has joined a guild server.")
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