package com.portalsoup.mrbutlertron.v2.core

import com.portalsoup.mrbutlertron.Environment
import com.portalsoup.mrbutlertron.v2.dsl.Command
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.File
import javax.script.ScriptEngineManager

object CommandAdapter : ListenerAdapter() {

    private val log = getLogger(javaClass)

    // TODO This needs to be replaced with a database table
    private val history: Array<String> = arrayOf("", "", "", "", "", "", "", "", "", "")
    private var currentIndex: Int = 0

    val loadedCommands: List<Command> = loadAll()

    private fun getCommandList(): Sequence<File> = File(Environment.commandsLocation)
        .takeIf { it.isDirectory }
        ?.walk()
        ?.filter { it.isFile }
        ?.filter { it.name.endsWith(".command.kts") }
        ?.onEach { log.debug("Found a file ${it.name}") }
        ?: emptySequence()

    fun get(name: String): Command? = loadedCommands.firstOrNull { it.name == name }

    private fun load(file: File): Command? = with(ScriptEngineManager().getEngineByExtension("kts")) {
        kotlin.runCatching { eval(file.readText()) }
            .onFailure {
                throw RuntimeException(
                    "Encountered an error compiling the command: ${file.name}",
                    it
                ).apply { stackTrace = emptyArray() }
            }
            .getOrNull()
            ?.takeIf { it is Command }
            ?.let { it as Command }
    }

    fun loadAll(): List<Command> = getCommandList()
        .mapNotNull { load(it) }
        .toList()
        .onEach { log.debug("Found ${it.name}") }

    fun incrementHistoryCounter() {
        if (currentIndex >= history.size - 1) {
            currentIndex = 0
        } else {
            currentIndex++
        }
    }


    // ***************************
    // * Discord adapter methods *
    // ***************************

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

        loadedCommands
            .filter { it.shouldRun(event) }
            .forEach { it.run(event) }
    }
}