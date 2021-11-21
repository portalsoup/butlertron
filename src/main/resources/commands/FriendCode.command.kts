import com.portalsoup.mrbutlertron.v2.core.formattedMessage
import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import com.portalsoup.mrbutlertron.v2.dsl.embed
import com.portalsoup.mrbutlertron.v2.dsl.embed.ListEmbed
import com.portalsoup.mrbutlertron.v2.manager.FriendCodeManager

command {
    name = "Friend Codes"
    description = ""
    command = "friendcode"

    //Break up into 3 jobs
    job("Operate on and request friend codes") {
        help {
            description = ""
            trigger = ""
            example { "" }
        }

        precondition { it.formattedMessage().startsWith("!friendcode") }
        action { message ->
            FriendCodeManager.parseCommandMessage(message)
                ?.takeUnless { message.formattedMessage().contains("help") }
                ?.run()
                ?.also { message.reply(it) }
                ?: embed {
                    author {
                        name = "Nintendo Friend Codes"
                    }

                    description = "Try one of these actions"

                    field {
                        name = "!friendcode @${message.author.name}"
                        inline = false
                        value = "Lookup a friend's code with a ping.  (Or none for your own)"
                    }

                    field {
                        name = "!friendcode add SW-1234-5678-9012"
                        inline = false
                        value = "Add a well-formed friend code"
                    }

                    field {
                        name = "!friendcode remove"
                        inline = false
                        value = "Remove your added friend code"
                    }
                }.also { message.reply(it) }
        }
    }
}