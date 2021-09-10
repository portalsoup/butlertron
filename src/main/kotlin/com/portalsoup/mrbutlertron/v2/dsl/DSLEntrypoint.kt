package com.portalsoup.mrbutlertron.v2.dsl

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

/**
 * DSL entrypoint for commands
 */
fun command(lambda: CommandBuilder.() -> Unit) = CommandBuilder().apply(lambda).build()


data class Command(
    val name: String,
    val description: String,
    val jobs: List<Job>
)

class CommandBuilder(
    var name: String = "",
    var description: String = "",
    private val jobs: MutableList<JobBuilder> = mutableListOf()
) {

    fun job(run: JobBuilder.() -> Unit) {
        JobBuilder().apply(run).also { jobs.add(it) }
    }

    internal fun build(): Command {
        return Command(
            name = name,
            description = description,
            jobs = jobs.map { it.build() }
        )
    }
}


/**
 * Job
 */
class Job(
    val run: (MessageReceivedEvent) -> Unit = { },
    val preconditions: MutableList<(MessageReceivedEvent) -> Boolean> = mutableListOf()
)

class JobBuilder {
    var run: (MessageReceivedEvent) -> Unit = { }
    var preconditions: MutableList<(MessageReceivedEvent) -> Boolean> = mutableListOf()

   internal fun build() = Job(run, preconditions)

    fun precondition(predicate: (MessageReceivedEvent) -> Boolean) {
        preconditions.add(predicate)
    }

    fun action(run: (MessageReceivedEvent) -> Unit) {
        this.run = run
    }

}