package me.konso.home_watchdog.plugins

import io.ktor.server.application.*
import io.ktor.server.engine.*
import me.konso.home_watchdog.Store
import java.util.UUID

fun Application.initShutdownURL() {
    val id = UUID.randomUUID().toString()
    val url = "/api/shutdown/$id"
    Store.shutdown = url
    install(ShutDownUrl.ApplicationCallPlugin) {
        shutDownUrl = url
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }
}