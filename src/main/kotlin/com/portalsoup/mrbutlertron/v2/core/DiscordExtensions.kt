package com.portalsoup.mrbutlertron.v2.core

import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

fun MessageReceivedEvent.reply(msg: String) {
    channel.sendMessage(msg).queue()
}

fun MessageReceivedEvent.reply(msg: MessageEmbed) {
    channel.sendMessage(msg).queue()
}

fun MessageReceivedEvent.formattedMessage(): String {
    return message
        .contentRaw
        .toLowerCase()
        .trim()
}