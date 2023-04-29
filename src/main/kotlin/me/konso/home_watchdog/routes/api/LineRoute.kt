package me.konso.home_watchdog.routes.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.LineCommonProperty
import me.konso.home_watchdog.events.followEvent
import me.konso.home_watchdog.events.receiveMessageEvent
import me.konso.home_watchdog.events.unfollowEvent
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
                    val json = Json.parseToJsonElement(body).jsonObject.toMap()

                    // Get events
                    val events = json["events"]?.jsonArray

                    // Action
                    if(events == null){
                        call.response.status(HttpStatusCode.BadRequest)
                        return@post
                    }
                    for(e in events){
                        val event = e.jsonObject.toMap()
                        val type = event[LineCommonProperty.TYPE_STRING.key].toString()
                                .toLowerCasePreservingASCIIRules().replace("\"", "", true)
                        logger.debug(type)
                        when(type){
                            "follow" -> { followEvent(event)  }
                            "unfollow" -> { unfollowEvent(event) }
                            "message" -> { receiveMessageEvent(event) }
                            else -> {}
                        }
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