package com.portalsoup.mrbutlertron.v2.dsl.embed

data class ListField(
    val name: String,
    val value: String
)

class ListEmbed(val header: String, val desc: String, val items: List<ListField>) {

    fun embed() {
        com.portalsoup.mrbutlertron.v2.dsl.embed {
            author {
                name = header
            }

            description = desc

            items.map {
                field {
                    name = it.name
                    inline = true
                    value = it.value
                }
            }
        }
    }
}