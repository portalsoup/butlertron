package com.portalsoup.mrbutlertron.data.util

import com.portalsoup.mrbutlertron.data.entity.DiscoverableEntity
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
import org.hibernate.service.ServiceRegistry
import org.reflections.Reflections
import java.util.*
import java.util.function.Consumer

object HibernateUtil {
    val sessionFactory: SessionFactory =
        try {
            val configuration = Configuration()
            val settings = Properties()
            settings[Environment.DRIVER] = "org.h2.Driver"
            settings[Environment.URL] = "jdbc:h2:./database/app"
            settings[Environment.USER] = "app"
            settings[Environment.PASS] = ""
            settings[Environment.DIALECT] = "org.hibernate.dialect.H2Dialect"
            settings[Environment.SHOW_SQL] = "true"
            settings[Environment.CURRENT_SESSION_CONTEXT_CLASS] = "thread"
            settings[Environment.HBM2DDL_AUTO] = "update"
            configuration.properties = settings
            registerEntities(configuration)
            val serviceRegistry: ServiceRegistry = StandardServiceRegistryBuilder()
                .applySettings(configuration.properties).build()
            configuration.buildSessionFactory(serviceRegistry)
        } catch (e: Exception) {
            throw RuntimeException("[Init Failed] Failed to create hibernate session factory", e)
        }


    private fun registerEntities(configuration: Configuration) {
        val reflections = Reflections("com.portalsoup.minimalhibernate.entity")
        reflections.getSubTypesOf(DiscoverableEntity::class.java).stream()
            .peek { x -> println(x) }
            .forEach { annotatedClass ->
                configuration.addAnnotatedClass(
                    annotatedClass
                )
            }
    }
}