package com.portalsoup.mrbutlertron.v2.dsl

import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import kotlin.reflect.KClass

//enum class EventTypes {
//    GuildMessageReceived,
//    GuildMemberJoined,
//    PrivateMessageReceived
//}

/**
 * Command
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
//data class JobContext(
//    private val respondTo: MutableList<KClass<Event>> = mutableListOf(),
//    private val preconditions: MutableList<(Event) -> Boolean> = mutableListOf()
//) {
//    fun <E: Event>listen(vararg events: KClass<out E>) {
//        respondTo + events
//    }
//
//    fun precondition(predicate: (Event) -> Boolean) {
//        preconditions.add(predicate)
//    }
//}

//class JobContextBuilder {
//    val respondTo: MutableList<out EventTypes> = mutableListOf()
//    val preconditions: MutableList<(Event) -> Boolean> = mutableListOf()
//
//    fun listen(vararg events: EventTypes) {
//        respondTo + events
//    }
//
//    fun precondition(predicate: (Event) -> Boolean) {
//        preconditions.add(predicate)
//    }
//}

class Job(
    val run: (MessageReceivedEvent) -> Unit = { Unit },
    val preconditions: MutableList<(MessageReceivedEvent) -> Boolean> = mutableListOf()
)

class JobBuilder {
    var run: (MessageReceivedEvent) -> Unit = { Unit }
    var preconditions: MutableList<(MessageReceivedEvent) -> Boolean> = mutableListOf()

   internal fun build() = Job(run, preconditions)

    fun precondition(predicate: (MessageReceivedEvent) -> Boolean) {
        preconditions.add(predicate)
    }

    fun action(run: (MessageReceivedEvent) -> Unit) {
        this.run = run
    }

}