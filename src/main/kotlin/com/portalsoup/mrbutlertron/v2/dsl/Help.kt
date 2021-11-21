package com.portalsoup.mrbutlertron.v2.dsl

import com.portalsoup.mrbutlertron.v2.core.EmbeddableEntity
import net.dv8tion.jda.api.entities.MessageEmbed


data class Help(
    val trigger: String,
    val desc: String,
    val examples: List<String>,
    val imageUrl: String?
): EmbeddableEntity {
    companion object {
        val empty = Help("", "", emptyList(), null)
    }

    override fun embed() = embed {
        author {
            name = trigger
        }

        image {
            url = url
        }

        field {
            value = desc
            inline = false
        }

        examples.map {
            field {
                value = it
            }
        }
    }
}

class HelpBuilder {

    var trigger: String = ""
    var description: String = ""
    private var imageUrl: String? = null
    private val examples: MutableList<String> = mutableListOf()

    fun example(exampleCommand: () -> String) {
        examples.add(exampleCommand())
    }

    fun imageUrl(url: () -> String) {
        examples.add(url())
    }

    fun build() = Help(trigger, description, examples, imageUrl)
}
