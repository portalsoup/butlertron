package com.portalsoup.mrbutlertron.commands.dsl

import net.dv8tion.jda.api.events.Event

@DslMarker annotation class CommandDsl

/*
 * Command
 */
fun <E : Event> command(lambda: CommandBuilder<E>.() -> Unit) = CommandBuilder<E>().apply(lambda).build()

data class Command<E : Event>(
    val job: Job<E>,
    val preconditions: List<(E) -> Boolean>,
    val description: String,
    val name: String
)

@CommandDsl
class CommandBuilder<E : Event> {
    private var job: Job<E> = Job { Unit }
    private var preconditions = mutableListOf<(E) -> Boolean>()
    private var description = ""
    private var name = ""

    fun job(lambda: JobBuilder<E>.() -> Unit) {
        job = JobBuilder<E>().apply(lambda).build()
    }

    fun preconditions(lambda: PreconditionListBuilder<E>.() -> Unit) {
        preconditions.addAll(
            PreconditionListBuilder<E>().apply(lambda).build()
        )
    }

    fun description(lambda: () -> String) {
        description = lambda()
    }

    fun name(lambda: () -> String) {
        name = lambda()
    }

    fun build() = Command(job, preconditions, description, name)
}

/*
 * Precondition
 */
@CommandDsl
class PreconditionListBuilder<E : Event> {
    private val preconditions = mutableListOf<(E) -> Boolean>()

    fun predicate(lambda: (E) -> Boolean) {
        preconditions.add(lambda)
    }

    fun build() = preconditions
}

/*
 * Job
 */
data class Job<in E : Event>(val run: (E) -> Unit)

@CommandDsl
class JobBuilder<E : Event> {
    private var run: (E) -> Unit = { _ -> Unit }

    fun run(lambda: (E) -> Unit) {
        this.run = lambda
    }

    fun build() = Job(run)
}