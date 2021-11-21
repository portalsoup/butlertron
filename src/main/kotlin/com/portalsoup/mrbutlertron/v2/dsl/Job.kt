package com.portalsoup.mrbutlertron.v2.dsl

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import com.portalsoup.mrbutlertron.Environment
import com.portalsoup.mrbutlertron.v2.core.formattedMessage

fun job(name: String, command: String, isHelpJob: Boolean, lambda: JobBuilder.() -> Unit): Job = JobBuilder(name, command, isHelpJob).apply(lambda).build()

data class Job(
    val name: String,
    val command: String,
    val run: (MessageReceivedEvent) -> Unit = { },
    val preconditions: MutableList<(MessageReceivedEvent) -> Boolean> = mutableListOf(),
    val help: Help
) {
    fun shouldRun(event: MessageReceivedEvent): Boolean = preconditions
        .toList()
        .all { condition -> condition(event) }
}

class JobBuilder(val name: String, val command: String, val isHelpJob: Boolean = false) {
    val commandSymbol = "${Environment.commandPrefix}$command"
    val prefix = Environment.commandPrefix
    var run: (MessageReceivedEvent) -> Unit = { }

    private val preconditions: MutableList<(MessageReceivedEvent) -> Boolean>

    init {
        preconditions = mutableListOf({ event: MessageReceivedEvent -> event.checkMessagePredicate { true } })

        if (! isHelpJob) {
            preconditions.add { event: MessageReceivedEvent ->
                ! event.formattedMessage().matches(".*help$".toRegex())
            }
        }
    }

    // TODO why can't this be lateinit without failing when compiled from scripts
    private val helpBuilder: HelpBuilder = HelpBuilder()

    val help: Help get() = helpBuilder.build()

    fun build() = Job(name, command, run, preconditions, help)

    fun precondition(predicate: (MessageReceivedEvent) -> Boolean) {
        preconditions.add(predicate)
    }

    fun preconditions(preconditions: List<(MessageReceivedEvent) -> Boolean>) {
        this.preconditions.addAll(preconditions)
    }

    fun action(run: (MessageReceivedEvent) -> Unit) {
        this.run = run
    }

    fun help(run: HelpBuilder.() -> Unit) {
        helpBuilder.apply(run)
    }

    fun MessageReceivedEvent.stripMessage() = formattedMessage()
        .takeIf { it.startsWith(commandSymbol) }
        ?.removePrefix(commandSymbol)
        ?.trim()
        ?.also { println("Before strip ${formattedMessage()}\n\tafter strip $it") }

    /**
     * Implicitly checks the bot command prefix and main command sequence in the message before stripping it
     * and running the result against the predicate.
     */
    fun MessageReceivedEvent.checkMessagePredicate(predicate: (strippedCommand: String) -> Boolean): Boolean {
        val result = stripMessage()
            ?.also { println("OK $it") }
            ?.let { predicate(it) }
            ?.also { println(it) }
            ?: false

        return result
    }
}