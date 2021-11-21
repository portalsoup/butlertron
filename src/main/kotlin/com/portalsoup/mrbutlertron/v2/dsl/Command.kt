package com.portalsoup.mrbutlertron.v2.dsl

import com.portalsoup.mrbutlertron.Environment
import com.portalsoup.mrbutlertron.v2.core.CommandAdapter
import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.getLogger
import com.portalsoup.mrbutlertron.v2.core.reply
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

/**
 * DSL entrypoint for commands
 */
fun command(lambda: CommandBuilder.() -> Unit) = CommandBuilder().apply(lambda).build()


data class Command(
    val name: String,
    val description: String,
    val jobs: List<Job>
) {
    private val log = getLogger(javaClass)

    fun shouldRun(event: MessageReceivedEvent): Boolean = jobs.any { it.shouldRun(event) }

    fun getJobsToRun(event: MessageReceivedEvent): List<Job> = jobs.filter { it.shouldRun(event) }

    fun run(event: MessageReceivedEvent) = getJobsToRun(event)
        .onEach { log.info("Executing job: $it") }
        .forEach { it.run(event) }
}

class CommandBuilder {
     var name: String = ""
     var description: String = ""
     var command: String = ""
    private val helpBuilder: HelpBuilder = HelpBuilder()
    val help: Help get() = helpBuilder.build()

    private val jobs: MutableList<Job> = mutableListOf()

    val prefix = Environment.commandPrefix

    fun job(name: String, run: JobBuilder.() -> Unit) {
        JobBuilder(name, command)
            .apply(run)
            .build()
            .also { jobs.add(it) }
    }

    fun help(run: HelpBuilder.() -> Unit) {
        helpBuilder.apply(run).build()
    }

    private fun buildHelpJobFor(job: Job): Job {
        return job("generated help job", job.command, true) {
            preconditions(job.preconditions)

            precondition { event ->
                event.formattedMessage().matches(".*help$".toRegex())
            }

            action { it.reply(job.help.embed()) }
        }
    }

    private fun buildOuterHelpJob(): Job {
        val triggerRegex = "^${prefix}${command}\\s+help$".toRegex()
        return job("generated command global help", command, true) {
            precondition { event ->
                val result = event.formattedMessage().matches(triggerRegex)
                println("The message was ${event.formattedMessage()} and it ${if (result) "matched" else "did not match"} the regex")
                result
            }

            action { it.reply(help.embed()) }
        }
    }

    internal fun build(): Command {
        val jobs = jobs.flatMap {
            listOf(
                it,
                buildHelpJobFor(it)
            )
        } + buildOuterHelpJob()

        return Command(
            name = name,
            description = description,
            jobs = jobs
        )
    }
}