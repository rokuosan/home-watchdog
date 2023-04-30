package me.konso.home_watchdog.events

import com.linecorp.bot.model.ReplyMessage
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.LineEvent
import java.io.File


suspend fun receiveMessageEvent(json: LineEvent){
    val logger = Store.Loggers.System
    val debugger = Store.Loggers.Debugger
    val client = Store.LINEBotClient
    val dao = Store.dao

    try{
        // Get user from database
        val user = dao.getUserById(json.source?.userId?:"")?:return

        // Switch process by authorization
        if(user.isAuthorized){
            client.replyMessage(
                ReplyMessage(
                    json.replyToken,
                    TextMessage("こんにちは")
                )
            )
        }else{
            val map = readInvitationCode().toMutableMap()

            // Check code
            var isValid = false
            val k = json.message?.text?:""
            val v = map[k]?:true
            if(!v){
                isValid = true
                map[k] = true
                updateInvitationCode(map)
                dao.authorize(user.id, true)

                logger.info("User[{}] was authorized by code {}", user.id, k)
            }

            val msg = if(!isValid){
                "無効な認証情報です。"
            }else{
                "認証されました。"
            }

            client.replyMessage(
                ReplyMessage(
                    json.replyToken,
                    TextMessage(msg)
                )
            )

        }

    }catch (ignored: Exception){
        logger.debug("Execution Failed: {}", json)
    }
}

fun readInvitationCode(): Map<String, Boolean>{
    return try{
        val file = File("invitation_codes.csv")
        val result = mutableMapOf<String, Boolean>()

        if(file.exists()){
            file.bufferedReader(Charsets.UTF_8).readLines().forEach {
                val s = it.split(",", limit = 2)
                val k = s[0].trim()
                val v = s[1].trim().toBooleanStrict()
                result[k] = v
            }
        }else{
            file.createNewFile()
            println("Invitation file not found.")
        }
        println(result)

        result.toMap()
    }catch (ignored: Exception){
        emptyMap()
    }
}

fun updateInvitationCode(map: Map<String, Boolean>){
    try{
        val file = File("invitation_codes.csv")

        file.bufferedWriter(Charsets.UTF_8).use { bw ->
            bw.write("")
            bw.flush()
            map.forEach { (k, v) ->
                bw.append("$k, $v")
                bw.newLine()
            }
        }
    }catch (ignored: Exception){
    }
}
