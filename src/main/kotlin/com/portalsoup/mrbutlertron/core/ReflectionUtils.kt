package com.portalsoup.mrbutlertron.core

import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import java.net.URL

class ReflectionUtils {

    companion object {
        private var log = getLogger(ReflectionUtils::class.java)

        private val configReflections by lazy {
            val urls: Collection<URL> = ClasspathHelper.forPackage("com.portalsoup.mrbutlertron", ClassLoader.getSystemClassLoader())
            log.info("urls:\n\n$urls\n\n")
            ConfigurationBuilder.build(urls)
        }

        fun reflections() = Reflections(configReflections)
    }
}