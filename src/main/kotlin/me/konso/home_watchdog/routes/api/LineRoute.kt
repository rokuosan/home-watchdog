package me.konso.home_watchdog.routes.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.LineWebhook
import me.konso.home_watchdog.utils.verifySignatures

fun Route.lineRoute(){
    val logger = Store.Loggers.Debugger

    /**
     * LINEサービスで利用されるエンドポイント
     */
    route("line"){
        /**
         * LINE BotのWebhookに登録するエンドポイント
         * ``x-line-signature``ヘッダーに署名を付与してリクエストを送信してください。
         */
        route("/callback"){
            post {
                try{
                    // Get Body
                    val body = call.receiveText()
                    logger.debug(body)

                    // Validation
                    if(!this.verifySignatures(body)){
                        call.response.status(HttpStatusCode.Unauthorized)
                        return@post
                    }

                    // Format Check
                    val json = Json.decodeFromString<LineWebhook>(body)

                    // Get events
                    val events = json.events

                    // Action
                    for(e in events){
                        e.executeEvent()
                    }

                    // Return
                    call.response.status(HttpStatusCode.OK)
                }catch (e: Exception){
                    // Return
                    call.response.status(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}