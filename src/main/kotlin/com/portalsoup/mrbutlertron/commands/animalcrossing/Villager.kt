package com.portalsoup.mrbutlertron.commands.animalcrossing

import com.portalsoup.discordbot.core.command.GuildMessageReceivedCommand
import com.portalsoup.discordbot.core.command.command
import com.portalsoup.mrbutlertron.core.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Villager : GuildMessageReceivedCommand<GuildMessageReceivedEvent>(

    command {

        name { "Villager lookup" }
        description { "`!villager {name}" }

        preconditions {
            predicate {
                it.message.contentRaw
                    .toLowerCase()
                    .trim()
                    .startsWith("!villager")
            }
        }

        job {
            addRunner { event ->
                val message = event.message.contentRaw

                val findArgs = Regex("-(\\S+)=").findAll(message)
                    .map { arg -> arg.groupValues[1] }

                val args = findArgs
                    .map { Pair(it, Regex("-$it=(\\S+)").find(message)?.groupValues?.get(1)) }
                    .toMap()

                println(args)

                if (args.values.isEmpty()) {
                    event.channel.sendMessage("""
                        >Provide one or more filters to find a villager
                        >Examples:
                        >    -name=bam
                        >    -species=deer
                        >    -personality=jock
                        >    
                        >Example command:
                        >    !villager -name=bam -species=deer
                    """.trimMargin(">")).queue()
                    return@addRunner
                }

                val maybeVillager = VillagerApi.lookupVillager(
                    name = args["name"],
                    species = args["species"]?.let { s -> Species.valueOf(s.toLowerCase().capitalize()) },
                    personality = args["personality"]?.let { p -> Personality.valueOf(p.toLowerCase().capitalize()) }

                )

                when (maybeVillager) {
                    is Try.Success -> {
                        maybeVillager.data.imageUrl?.let { img -> event.channel.sendMessage(img) }
                        maybeVillager.data
                            .let { villager -> "${villager.url}" }
                            .let { event.channel.sendMessage(it).queue() }
                    }
                    is Try.Failure -> {
                        println(maybeVillager)
                        when (maybeVillager.error) {
                            is TryFailed -> {
                                println("about to reply failure")
                                val raw = maybeVillager.error.reason
                                if (raw.length > 1999) {
                                    event.channel.sendMessage(raw.substring(0, 1998)).queue()
                                } else {
                                    event.channel.sendMessage(raw).queue()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
)