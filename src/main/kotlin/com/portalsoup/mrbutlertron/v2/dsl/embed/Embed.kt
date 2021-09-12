package com.portalsoup.mrbutlertron.v2.dsl

import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role

/**
 * DSL entrypoint for message embed objects
 */
fun embed(lambda: EmbedDSLBuilder.() -> Unit) = EmbedDSLBuilder().apply(lambda).build()

class EmbedDSLBuilder {
    var url: String? = null
    var title: String? = null
    var color: Int = Role.DEFAULT_COLOR_RAW
    var description: String? = null
    var addBlankField: Boolean = false
    var author: MessageEmbed.AuthorInfo? = null
    var footer: MessageEmbed.Footer? = null
    var imageInfo: MessageEmbed.ImageInfo? = null
    var thumbnail: MessageEmbed.Thumbnail? = null
    var fields: MutableList<MessageEmbed.Field> = mutableListOf()
    var embedType: EmbedType? = null

    fun author(lambda: AuthorBuilder.() -> Unit) = AuthorBuilder().apply(lambda).build().also { this.author = it }
    fun footer(lambda: FooterBuilder.() -> Unit) = FooterBuilder().apply(lambda).build().also { this.footer = it }
    fun image(lambda: ImageBuilder.() -> Unit) = ImageBuilder().apply(lambda).build().also { this.imageInfo = it }
    fun thumbnail(lambda: ThumbnailBuilder.() -> Unit) =
        ThumbnailBuilder().apply(lambda).build().also { this.thumbnail = it }

    fun field(lambda: FieldBuilder.() -> Unit) = FieldBuilder().apply(lambda).build().also { fields.add(it) }

    fun build() = MessageEmbed(
        url,
        title,
        description,
        embedType,
        null,
        color,
        thumbnail,
        null,
        author,
        null,
        footer,
        imageInfo,
        fields
    )
}

class AuthorBuilder {
    var name: String? = null
    var url: String? = null
    var iconUrl: String? = null
    val proxyIconUrl: String? = null

    fun build() = MessageEmbed.AuthorInfo(name, url, iconUrl, proxyIconUrl)
}

class FooterBuilder {

    var text: String? = null
    var iconUrl: String? = null
    var proxyIconUrl: String? = null

    fun build() = MessageEmbed.Footer(text, iconUrl, proxyIconUrl)
}

class ImageBuilder {

    var url: String? = null
    var proxyUrl: String? = null
    var width = 0
    var height = 0

    fun build() = MessageEmbed.ImageInfo(url, proxyUrl, width, height)
}

class ThumbnailBuilder {

    var url: String? = null
    var proxyUrl: String? = null
    var width = 0
    var height = 0

    fun build() = MessageEmbed.Thumbnail(url, proxyUrl, width, height)
}

class FieldBuilder {

    var name: String = ""
    var value: String = ""
    var inline = false
    var checked = true

    fun build() = MessageEmbed.Field(name, value, inline, checked)
}