package me.konso.home_watchdog

import io.ktor.server.application.*
import me.konso.home_watchdog.plugins.configureLogging
import me.konso.home_watchdog.plugins.configureRouting

fun main(args: Array<String>): Unit=
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureLogging()
    configureRouting()
}
