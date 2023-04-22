package me.konso.home_watchdog.events

import com.linecorp.bot.model.ReplyMessage
import com.linecorp.bot.model.message.TextMessage
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import me.konso.home_watchdog.Store

fun PipelineContext<*, ApplicationCall>.followEvent(json:  Map<String, JsonElement>){
    val client = Store.LINEBotClient
    val replyToken = json["replyToken"]?.jsonPrimitive?.content?:""

    Store.SystemLogger.info(replyToken)

    try{
        val res = client.replyMessage(ReplyMessage(
            replyToken,
            TextMessage("Hello!")
        )).get()

        Store.SystemLogger.debug(res.toString())
    }catch (ignored: Exception){
    }
}
