package com.portalsoup.mrbutlertron.v2

import com.portalsoup.mrbutlertron.v2.core.CommandAdapter
import com.portalsoup.mrbutlertron.v2.core.getLogger
import com.portalsoup.mrbutlertron.v2.data.DatabaseFactory
import net.dv8tion.jda.api.JDABuilder

class Bot(val name: String, val token: String) {

    private val log = getLogger(javaClass)

    fun isEnumTypeValid(enumClass: Class<*>, value: String) = enumClass.enumConstants
            ?.let { it as Array<Enum<*>> }
            ?.firstOrNull { it.name.equals(value, true) } != null

    val jda by lazy {
        val token = token
        log.debug("token: ${token}")

        JDABuilder.createDefault(token)
            .addEventListeners(CommandAdapter(this))
            .setAutoReconnect(true)
            .build()
            .awaitReady()
    }

    fun init() {
        log.info("Initializing $name...")
        DatabaseFactory.init()
    }

    fun run() {
        jda // initialize it now
        log.info("$name ready for service!")
    }
}