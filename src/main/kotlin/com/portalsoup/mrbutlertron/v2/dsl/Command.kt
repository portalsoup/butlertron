package com.portalsoup.mrbutlertron.v2.dsl

import com.portalsoup.mrbutlertron.v2.core.getLogger
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

/**
 * DSL entrypoint for commands
 */
fun command(lambda: CommandBuilder.() -> Unit) = CommandBuilder().apply(lambda).build()


data class Command(
    val name: String,
    val description: String,
    val jobs: List<Job>,
    val help: Help
) {
    private val log = getLogger(javaClass)

    fun shouldRun(event: MessageReceivedEvent): Boolean = jobs.any { it.shouldRun(event) }

    fun run(event: MessageReceivedEvent) = jobs
        .filter { it.shouldRun(event) }
        .forEach { it.run(event) }
}

class CommandBuilder {

    lateinit var name: String
    lateinit var description: String
    lateinit var help: Help
    private val jobs: MutableList<Job> = mutableListOf()

    fun job(run: JobBuilder.() -> Unit) {
        JobBuilder().apply(run).let { it.build() }.also { jobs.add(it) }
    }

    fun help(run: HelpBuilder.() -> Unit) {
        HelpBuilder().apply(run).let { it.build() }.also { help = it }
    }

    internal fun build(): Command {
        return Command(
            name = name,
            description = description,
            jobs = jobs,
            help = help
        )
    }
}

data class Help(
    val trigger: String,
    val desc: String,
    val jobs: List<Action>
) {
    fun embed() = embed {

        author {
            name = trigger
        }
        field {
            value = desc
            inline = false
        }

        jobs.map { action ->
            val examples = action
                .examples
                .takeIf { it.isNotEmpty() }?.let { "\n\nExamples:\n${it.joinToString("\n")}" }
                ?: ""
            field {
                name = action.cmd
                value = "${action.desc}$examples"
            }
        }
    }
}

class HelpBuilder {

    lateinit var trigger: String
    lateinit var description: String
    private val actions: MutableList<Action> = mutableListOf()

    fun action(cmd: String, desc: String, vararg examples: String) {
        actions += Action(cmd, desc, examples.toList())
    }

    fun build() = Help(trigger, description, actions.toList())
}

data class Action(val cmd: String, val desc: String, val examples: List<String>)