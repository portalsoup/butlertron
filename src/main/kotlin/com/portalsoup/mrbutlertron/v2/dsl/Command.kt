package com.portalsoup.mrbutlertron.v2.dsl

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