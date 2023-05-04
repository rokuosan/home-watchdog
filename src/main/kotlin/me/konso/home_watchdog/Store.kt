package me.konso.home_watchdog

import com.aallam.openai.client.OpenAI
import com.linecorp.bot.client.LineMessagingClient
import io.ktor.server.config.*
import me.konso.home_watchdog.database.dao.DaoFacade
import me.konso.home_watchdog.entities.config.DatabaseConfig
import me.konso.home_watchdog.entities.config.TargetsConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Store {
    lateinit var LINEBotClient: LineMessagingClient
    lateinit var dao: DaoFacade
    lateinit var shutdown: String
    lateinit var config: ApplicationConfig
    lateinit var openai: OpenAI

    val ServerStatus: MutableMap<String, Boolean> = mutableMapOf()

    object Defaults{
        val DATABASE_CONFIG = DatabaseConfig(
            databaseDriver = "org.postgresql.Driver",
            databaseUrl = "jdbc:postgresql://localhost:5432/postgres",
            databaseUser = "postgres",
            databasePass = "postgres"
        )

        val TARGETS_CONFIG = TargetsConfig(
            servers = emptyList(),
            interval = 10_000,
            timeout = 5_000
        )
    }

    object Loggers{
        val System: Logger = LoggerFactory.getLogger("System")
        val Debugger: Logger = LoggerFactory.getLogger("Debugger")
    }
}