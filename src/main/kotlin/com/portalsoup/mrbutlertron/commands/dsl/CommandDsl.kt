package com.portalsoup.mrbutlertron.commands.dsl

import net.dv8tion.jda.api.events.Event

@DslMarker annotation class CommandDsl

fun <E : Event> command(lambda: CommandBuilder<E>.() -> Unit) = CommandBuilder<E>().apply(lambda).build()

data class Command<E : Event>(
    val job: Job<E>,
    val preconditions: List<Precondition<E>>
)

@CommandDsl
class CommandBuilder<E : Event> {
    private var job: Job<E> =
        Job { Unit }
    private var preconditions = mutableListOf<Precondition<E>>()

    fun job(lambda: JobBuilder<E>.() -> Unit) {
        job = JobBuilder<E>().apply(lambda).build()
    }

    fun preconditions(lambda: PreconditionListBuilder<E>.() -> Unit) {
        preconditions.addAll(
            PreconditionListBuilder<E>().apply(lambda).build()
        )
    }

    fun build() = Command(job, preconditions)
}

data class Precondition<in E : Event>(val predicate: (E) -> Boolean)

@CommandDsl
class PreconditionListBuilder<E : Event> {
    private val preconditions = mutableListOf<Precondition<E>>()

    fun precondition(lambda: PreconditionBuilder<E>.() -> Unit) {
        preconditions.add(
            PreconditionBuilder<E>().apply(lambda).build()
        )
    }

    fun build() = preconditions
}

@CommandDsl
class PreconditionBuilder<E : Event> {
    private var predicate: (E) -> Boolean = { _ -> false }

    fun predicate(lambda: (E) -> Boolean) {
        this.predicate = lambda
    }

    fun build() = Precondition(predicate)
}

data class Job<in E : Event>(val run: (E) -> Unit)

@CommandDsl
class JobBuilder<E : Event> {
    private var run: (E) -> Unit = { _ -> Unit }

    fun run(lambda: (E) -> Unit) {
        this.run = lambda
    }

    fun build() = Job(run)
}