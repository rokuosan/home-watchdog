package me.konso.home_watchdog

import com.linecorp.bot.client.LineMessagingClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Store {
    val SystemLogger: Logger= LoggerFactory.getLogger("System")
    lateinit var LINEBotClient: LineMessagingClient
}