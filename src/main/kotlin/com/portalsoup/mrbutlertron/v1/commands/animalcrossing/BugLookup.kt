//package com.portalsoup.mrbutlertron.v1.commands.animalcrossing
//
//import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
//import com.portalsoup.discordbot.core.command.command
//import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
//
//class BugLookup : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(
//
//        command {
//
//            name { "Bug lookup" }
//            description { "`!bug {name}" }
//
//            preconditions {
//                predicate {
//                    it.message.contentRaw
//                        .toLowerCase()
//                        .trim()
//                        .startsWith("!villager")
//                }
//            }
//
//        }
//    )