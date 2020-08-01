package com.portalsoup.mrbutlertron.core

import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder

class ReflectionUtils {

    companion object {
        private var log = getLogger(ReflectionUtils::class.java)

        fun reflections() = Reflections("com.portalsoup.mrbutlertron.commands")
    }
}