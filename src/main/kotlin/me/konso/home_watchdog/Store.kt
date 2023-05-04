package me.konso.home_watchdog

import com.linecorp.bot.client.LineMessagingClient
import io.ktor.server.config.*
import me.konso.home_watchdog.database.dao.DaoFacade
import me.konso.home_watchdog.entities.config.DatabaseConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Store {
    lateinit var LINEBotClient: LineMessagingClient
    lateinit var dao: DaoFacade
    lateinit var shutdown: String
    lateinit var config: ApplicationConfig

    object Defaults{
        val DATABASE_CONFIG = DatabaseConfig(
            databaseDriver = "org.postgresql.Driver",
            databaseUrl = "jdbc:postgresql://localhost:5432/postgres",
            databaseUser = "postgres",
            databasePass = "postgres"
        )
    }

    object Loggers{
        val System: Logger = LoggerFactory.getLogger("System")
        val Debugger: Logger = LoggerFactory.getLogger("Debugger")
    }
}