package me.konso.home_watchdog.events

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.LineCommonProperty

/**
 * 友達解除時のイベントハンドラ
 *
 * 主にデータベースから該当のユーザーデータを削除します。
 */
suspend fun unfollowEvent(json: Map<String, JsonElement>){
    val logger = Store.SystemLogger
    val dao = Store.dao

    try{
        // Get user id
        val userId = json[LineCommonProperty.SOURCE_OBJECT.key]!!.jsonObject["userId"]!!.jsonPrimitive.content
        logger.debug("UserID: $userId")

        // Delete
        val res = dao.deleteUser(userId)
        val msg = if(res){
            """
                DBから[${userId}]のデータを削除しました。
            """.trimIndent()
        }else{
            """
                削除処理に失敗しました。[$userId]
            """.trimIndent()
        }
        logger.debug(msg)

    }catch (ignored: Exception){
    }
}