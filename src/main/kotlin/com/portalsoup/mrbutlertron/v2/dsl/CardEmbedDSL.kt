package com.portalsoup.mrbutlertron.v2.dsl

fun cardEmbed(lambda: CardBuilder.() -> Unit) = CardBuilder().apply(lambda).build()

class CardBuilder {

    var uri: String = ""
    lateinit var cardName: String
    var costLabel: String = ""
    var cost: String = ""
    var oracleText: String = ""
    var flavorText: String = ""
    var cardImage: String = ""
    var artCrop: String = ""
    var atkLabel: String = ""
    var defLabel: String = ""
    var atk: String = ""
    var def: String = ""
    var spellType: String = ""

    fun build() = embed {

        description = uri

        if (cardImage.isNotEmpty()) {
            thumbnail {
                url = cardImage
            }
        }

        field {
            value = cardName
            inline = true
        }

        if (spellType.isNotEmpty()) {
            field {
                value = spellType
                inline = true
            }
        }

        if (oracleText.isNotEmpty()) {
            field {
                value = oracleText
                inline = false
            }
        }

        if (cost.isNotEmpty()) {
            field {
                name = costLabel
                value = cost
                inline = true
            }
        }

        if (atk.isNotEmpty() || def.isNotEmpty()) {
            field {
                name = listOf(atkLabel, defLabel)
                    .filter { it.isNotEmpty() }
                    .joinToString("/")
                value = listOf(atk, def)
                    .filter { it.isNotEmpty() }
                    .joinToString("/")
                inline = true
            }
        }

        image {
            url = artCrop
        }

        footer {
            text = flavorText
        }
    }
}