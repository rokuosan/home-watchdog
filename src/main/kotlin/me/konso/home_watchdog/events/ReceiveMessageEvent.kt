package me.konso.home_watchdog.events

import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.LineEvent


suspend fun receiveMessageEvent(json: LineEvent){
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

    try{
        debugger.debug(json.toString())
    }catch (e: Exception){
        e.printStackTrace()
    }
}
