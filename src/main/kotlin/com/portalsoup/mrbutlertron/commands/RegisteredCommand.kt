package com.portalsoup.mrbutlertron.commands

public enum class RegisteredCommand(
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