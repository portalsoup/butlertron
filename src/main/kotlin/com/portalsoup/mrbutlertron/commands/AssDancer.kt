package com.portalsoup.mrbutlertron.commands

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class AssDancer : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    command {
        preconditions {
            predicate {
                it.message.contentRaw
                    .trim()
                    .toLowerCase()
                    .matches(Regex("do\\sone\\sof\\syour\\slittle\\sdances\\sfor\\s(.*)."))
            }
        }

        job {
            run {
                it.channel
                    .sendMessage("A traditional Ass-Dance will cost you *TWO* boxes of Chocobees.")
                    .queue()
            }
        }
    }
)