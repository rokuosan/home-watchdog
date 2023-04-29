package me.konso.home_watchdog.events

import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.LineEvent

/**
 * 友達解除時のイベントハンドラ
 *
 * 主にデータベースから該当のユーザーデータを削除します。
 */
suspend fun unfollowEvent(json: LineEvent){
    val logger = Store.Loggers.System
    val dao = Store.dao

    try{
        // Get user id
        val userId = json.source?.userId?:""
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