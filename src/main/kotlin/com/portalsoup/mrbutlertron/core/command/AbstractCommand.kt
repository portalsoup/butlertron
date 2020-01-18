package com.portalsoup.mrbutlertron.core.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.Event

/**
 *
 * @param E The discord event an implementing command intends to hook with
 */
abstract class AbstractCommand<E : Event> {

    val unexpectedFailureMessage = "Caught an unexpected message"
    val runPredicates = emptyList<(event: E) -> Boolean>().toMutableList()

    /**
     * Add a predicate to this command's criteria.  Each predicate should return true if their criteria are satisfied.
     */
    fun addPrecondition(predicate: (event: E) -> Boolean) {
        runPredicates += predicate
    }

    protected abstract fun run(event: E)

    /**
     * Wraps the run(event) function to add exception handling and pre-condition validation.
     */
    fun tryRun(event: E): Unit =
        try {
            val isRunCommand = runPredicates
                .map {
                    println("[${this::class.java.simpleName}]Evaluating ${it(event)}")
                    it(event)
                }
                .fold(true) { willReply, nextPredicate -> willReply && nextPredicate} // once false stays false

            when {
                isRunCommand -> run(event)
                else -> { }
            }
        } catch (e: Exception) {
            println(unexpectedFailureMessage + ": " + e.message)
        }

    // helpers
    fun prefixPredicate(full: String, prefix: String): Boolean {
        return full
            .toLowerCase()
            .trim()
            .startsWith(prefix)
    }


    fun hasPermissionToPost(self: Member, channel: TextChannel) : Boolean {
        return self.hasPermission(channel, Permission.MESSAGE_WRITE)
    }
}