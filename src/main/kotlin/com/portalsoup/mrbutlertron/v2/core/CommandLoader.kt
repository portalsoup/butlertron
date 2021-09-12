package com.portalsoup.mrbutlertron.v2.core

import com.portalsoup.mrbutlertron.Environment
import com.portalsoup.mrbutlertron.v2.Bot
import com.portalsoup.mrbutlertron.v2.dsl.Command
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.File
import javax.script.ScriptEngineManager

class CommandLoader(
    private val bot: Bot,
    private val loadedCommands: List<Command> = load(),

    // TODO This needs to be replaced with a database table
    private val history: Array<String> = arrayOf("","","","","","","","","",""),
    private var currentIndex: Int = 0

): ListenerAdapter() {

    private val log = getLogger(javaClass)

    companion object {
        private val log = getLogger(javaClass)

        fun load(): List<Command> = File(Environment.commandsLocation)
                .takeIf { it.isDirectory }
                ?.walk()
                ?.filter { it.isFile }
                ?.filter { it.name.endsWith(".command.kts") }
                ?.onEach { log.debug("Found a file ${it.name}") }
                ?.map { with(ScriptEngineManager().getEngineByExtension("kts")) { eval(it.readText()) }}
                ?.filter { it != null && it is Command }
                ?.onEach { log.debug("Found $it") }
                ?.mapNotNull { when (it) {
                    is Command -> it as Command
                    else -> null
                }}
                ?.toList()
            ?: emptyList()
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        log.debug("Message received! $event")
        val messageId = event.messageId

        if (history.contains(messageId)) {
            log.debug("Encountered an event with a duplicate message ID [$messageId]")
            return
        } else {
            log.debug("Adding $messageId to index=$currentIndex")
            history[currentIndex] = messageId
            incrementHistoryCounter()
        }

        loadedCommands.flatMap { command ->
            command.jobs
                .filter {
                    log.debug("Filtering a job by preconditions")
                    it.preconditions.toList().any { condition ->
                        log.debug("running preconditions $condition")
                        condition(event).also { res -> log.debug("Precondition: $res") }
                    }
                }
        }
            .onEach { log.debug("Survived! $it") }
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