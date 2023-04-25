package me.konso.home_watchdog

import com.linecorp.bot.client.LineMessagingClient
import me.konso.home_watchdog.database.dao.DaoFacade
import me.konso.home_watchdog.entities.DatabaseConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Store {
    val SystemLogger: Logger= LoggerFactory.getLogger("System")
    lateinit var LINEBotClient: LineMessagingClient
    lateinit var dao: DaoFacade

    object Defaults{
        val DATABASE_CONFIG = DatabaseConfig(
            databaseDriver = "org.postgresql.Driver",
            databaseUrl = "jdbc:postgresql://localhost:5432/postgres",
            databaseUser = "postgres",
            databasePass = "postgres"
        )
    }
}