package com.portalsoup.discordbot.core.command

import net.dv8tion.jda.api.events.Event

abstract class AbstractCommandBuilder<E : Event> {

    internal var job: Job<E> = Job(listOf())
    internal var preconditions = mutableListOf<(E) -> Boolean>()
    internal var description = ""
    internal var name = ""

     fun description(lambda: () -> String) {
        description = lambda()
    }

     fun name(lambda: () -> String) {
        name = lambda()
    }


     fun build(): Command<E> =
        Command(name = name, description = description, job = job, preconditions = preconditions)
}