package com.portalsoup.mrbutlertron.commands.shinchan

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.discordbot.core.command.sendMessage
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class AssDancer : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    sendMessage {
        preconditions {
            predicate {
                it.message.contentRaw
                    .trim()
                    .toLowerCase()
                    .matches(Regex("do\\sone\\sof\\syour\\slittle\\sdances\\sfor\\s(.*)."))
            }
        }

        job {
            reply { "A traditional Ass-Dance will cost you *TWO* boxes of Chocobees." }
        }
    }
)