package com.portalsoup.mrbutlertron.v2.dsl

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

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