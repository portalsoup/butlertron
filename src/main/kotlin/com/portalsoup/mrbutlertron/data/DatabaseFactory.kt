package com.portalsoup.mrbutlertron.data

import com.portalsoup.mrbutlertron.data.entity.RememberMe
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    val dataSource = "jdbc:h2:./database/app"

    fun init() {
        val flyway = Flyway.configure().dataSource(DatabaseFactory.dataSource, "bot", null).load()
        migrateFlyway(flyway)

        Database.connect(hikari())
        transaction {
            create(RememberMe)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction { block() }

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
            flyway.validate()
            flyway.migrate()
        } catch (e: FlywayException) {
            flyway.repair()
            if (runAgain) {
                migrateFlyway(flyway, false)
            } else {
                throw e
            }
        }
    }
}