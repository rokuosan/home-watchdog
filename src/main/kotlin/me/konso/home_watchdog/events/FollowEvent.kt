package me.konso.home_watchdog.events

import com.linecorp.bot.model.ReplyMessage
import com.linecorp.bot.model.message.TextMessage
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.LineCommonProperty

suspend fun followEvent(json:  Map<String, JsonElement>){
    val logger = Store.Loggers.System
    val client = Store.LINEBotClient
    val dao = Store.dao

    try{
        // Get reply token
        val replyToken = json["replyToken"]?.jsonPrimitive?.content?:""
        logger.debug(json.toString())

        // Get user id
        val userId = json[LineCommonProperty.SOURCE_OBJECT.key]!!.jsonObject["userId"]!!.jsonPrimitive.content
        logger.debug("UserID: $userId")


        // Add this user to database
        val user = dao.getUserById(userId)?:dao.addUser(userId)
        val msg = if(user == null){
            logger.info("Failed to add user. [$userId]")
            """
                内部処理に失敗しました。
                このアカウントを一度ブロックしたのち、再度友達追加してください。
            """.trimIndent()
        }else{
            """
                認証情報を送信してください。
            """.trimIndent()
        }

        // Send message
        val res = client.replyMessage(ReplyMessage(
            replyToken,
            TextMessage(msg)
        )).get()
        logger.debug(res.toString())
    }catch (ignored: Exception){
    }
}
