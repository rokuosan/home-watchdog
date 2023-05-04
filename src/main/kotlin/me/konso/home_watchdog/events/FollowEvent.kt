package me.konso.home_watchdog.events

import com.linecorp.bot.model.ReplyMessage
import com.linecorp.bot.model.message.TextMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.line.LineEvent

suspend fun followEvent(json: LineEvent){
    val logger = Store.Loggers.System
    val client = Store.LINEBotClient
    val dao = Store.dao

    try{
        // Get reply token
        val replyToken = json.replyToken
        logger.debug(json.toString())

        // Get user id
        val userId = json.source?.userId?:""
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
        val res = withContext(Dispatchers.IO) {
            client.replyMessage(
                ReplyMessage(
                    replyToken,
                    TextMessage(msg)
                )
            ).get()
        }
        logger.debug(res.toString())
    }catch (ignored: Exception){
    }
}
