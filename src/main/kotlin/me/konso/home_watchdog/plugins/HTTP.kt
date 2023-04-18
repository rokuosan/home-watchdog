package me.konso.home_watchdog.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureHTTP(){
    // CORS
    install(CORS){
        // Reference: https://ktor.io/docs/cors.html
        allowHeader(HttpHeaders.ContentType)
        // Reference: https://developers.line.biz/ja/reference/messaging-api/#request-headers
        allowHeader("x-line-signature")
        anyHost()
    }

    // Swagger
    routing {
        swaggerUI(path = "openapi")
    }

}