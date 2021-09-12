package com.portalsoup.mrbutlertron.v2.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {

    val dataSource = "jdbc:h2:./database/app"

    fun init() {
        val flyway = Flyway.configure().dataSource(dataSource, "bot", null).load()
        migrateFlyway(flyway)

        Database.connect(hikari())
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = dataSource
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.username = "bot"
        config.validate()
        return HikariDataSource(config)
    }

    fun migrateFlyway(flyway: Flyway, runAgain: Boolean = true) {
        try {
            flyway.migrate()
        } catch (e: Exception) {
            flyway.repair()
            flyway.validate()
            if (runAgain) {
                migrateFlyway(flyway, false)
            } else {
                throw RuntimeException("Failed to migrate or repair flyway", e)
            }
        }
    }
}