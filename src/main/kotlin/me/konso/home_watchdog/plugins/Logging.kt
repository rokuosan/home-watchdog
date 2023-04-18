package me.konso.home_watchdog.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureLogging(){
    install(CallLogging){
        level = Level.INFO

        filter { call ->
            call.request.path().startsWith("/")
        }

        format { call ->
            val status = call.response.status()
            val method = call.request.httpMethod
            val ua = call.request.headers["User-Agent"]
            val path = call.request.path()
            "${method.value} $status $path [$ua]"
        }
    }
}