package com.portalsoup.mrbutlertron.commands.messagereceived.usercommands

import com.portalsoup.mrbutlertron.commands.messagereceived.GuildMessageReceivedCommand
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.lang.StringBuilder

class CommandList : GuildMessageReceivedCommand() {

    init {
        addPrecondition {
            val sanitized = it.message.contentRaw.trim().toLowerCase()
            val matches = sanitized.contains("help") &&
                    sanitized.contains("mr") &&
                    sanitized.contains("butlertron")
            matches
        }
    }

    fun getCommandList(): String {
        try {
            val cmdList = StringBuilder("These are my commands:\n\n")
            RegisteredCommand.values()
                .map { formatCommand(it) }
                .forEach { cmdList.append(it)}

            return cmdList.toString()
        } catch (e: RuntimeException) {
            println("Failed: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    override fun run(event: GuildMessageReceivedEvent) {
        event.channel.sendMessage(getCommandList()).queue()
    }

    private fun formatCommand(command: RegisteredCommand): String {
        println(command)
        return "${command.commandName} - ${command.description}${if (command.additionalDetails != null) "\n -- ${command.additionalDetails}" else ""}\n\n"
    }

    /**
     * Only commands defined by this enum will show up in the public help.  A command can be made more secret by
     * omitting it from this enum.  Consequently, omitted commands will only be organically discoverable.
     */
    private enum class RegisteredCommand(
        val commandName: String,
        val description: String,
        val additionalDetails: String?) {

        GREET_NEW_MEMBERS("Greet New Members",
            "I do this automatically",
            null),
        BUTLERTRON_QUOTES("Butlertron Quotes",
            "Say my name.",
            null),
        YGO_LOOKUP("YuGiOh Card Lookup",
            "`ygo {card-name}`",
            "When punctuation is involved, exact formatting must be used around it!"),
        MTG_LOOKUP("MTG Card Lookup",
            "`mtg {card-name}`",
            null),
        CHUCK_NORRIS("Chuck Norris",
            "Barrens chat is best chat",
            null)
    }
}