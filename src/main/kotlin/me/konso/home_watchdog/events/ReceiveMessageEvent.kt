package me.konso.home_watchdog.events

import com.linecorp.bot.parser.WebhookParser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.EventObject
import me.konso.home_watchdog.entities.WebhookObject


suspend fun receiveMessageEvent(json: Map<String, JsonElement>){
    val logger = Store.Loggers.System
    val debugger = Store.Loggers.Debugger
    val client = Store.LINEBotClient
    val dao = Store.dao

    try{
//        val j = Json.decodeFromString<WebhookObject>(Json.encodeToString(json))
//        debugger.info(j.events[0].replyToken)
//        val replyToken = json["replyToken"]?.jsonPrimitive?.content?:""
//        debugger.debug("ReplyToken: $replyToken")



    }catch (ignored: Exception){

    }

    val rawString = Json.encodeToString(json)
    debugger.debug(rawString)
    try{
        val webhook = Json.decodeFromString<EventObject>(rawString)
        debugger.debug(webhook.toString())
    }catch (e: Exception){
        e.printStackTrace()
    }
}
