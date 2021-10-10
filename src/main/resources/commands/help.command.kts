import com.portalsoup.mrbutlertron.v2.core.CommandAdapter
import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command

command {
    name = "Help"
    description = "Provides information on available commands"

    help {
        description = "Information about commands"
        trigger = "!help"

        action(
            "!help list",
            "List all available commands"
        )

        action(
            "!help {command}",
            "Get help with a specific command.  Use the command name without the ! prefix",
            "!help mtg", "!help villager"
        )
    }

    job {
        precondition { it.formattedMessage() == "!help" }
        action { event -> CommandAdapter.get("help")?.help?.embed()?.let { event.reply(it) } }
    }

    job {
        val matchCommand: Regex = "!help\\s+(\\w+)".toRegex()

        precondition { it.formattedMessage().matches(matchCommand) }
        action { event ->
            val cmd: String? = matchCommand.find(event.formattedMessage())?.groups?.get(1)?.value
            println(cmd)

            cmd
                ?.let { CommandAdapter.get(cmd) }
                ?.help
                ?.embed()
                ?.let { event.reply(it) }
        }
    }
}