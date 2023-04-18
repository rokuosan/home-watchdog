package me.konso.home_watchdog.routes.api

import com.linecorp.bot.parser.WebhookParser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.lineRoute(){

    route("line"){
        post("/callback"){

        }
    }
}