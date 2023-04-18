package me.konso.home_watchdog.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import me.konso.home_watchdog.routes.api.lineRoute

fun Application.configureRouting() {
    install(ContentNegotiation){
        json()
    }

    routing {
        get("/") {
            call.respondText("Home Watchdog v0.1")
        }

        route("/api/"){
            lineRoute()
        }
    }
}
