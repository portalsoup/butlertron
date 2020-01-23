package com.portalsoup.mrbutlertron

import com.portalsoup.mrbutlertron.commands.dsl.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.reflections.Reflections
@Suppress("UNCHECKED_CAST")
class EventListener(private val bot: MrButlertron) : ListenerAdapter() {

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
            .forEach { it.run(event) }
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
            .forEach { it.run(event) }
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
        // dsl commands
        val reflections = Reflections()

        val dslCommands: List<GuildJoinCommand<*>> =
            reflections.getSubTypesOf(GuildJoinCommand::class.java)
                .map { it.getConstructor().newInstance() }

        dslCommands.filter {

            val predicates = it.command.preconditions as List<(GuildJoinEvent) -> Boolean>

            predicates
                .map { predicate -> predicate(event) }
                .fold(true) {total, next -> total && next}
        }.map { it.command.job as Job<GuildJoinEvent> }
            .forEach { it.run(event) }
    }

    fun incrementHistoryCounter() {
        if (currentIndex >= history.size - 1) {
            currentIndex = 0
        } else {
            currentIndex++
        }
    }
}