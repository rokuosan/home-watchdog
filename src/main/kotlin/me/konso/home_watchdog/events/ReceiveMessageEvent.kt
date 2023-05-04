package me.konso.home_watchdog.events

import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.linecorp.bot.model.ReplyMessage
import com.linecorp.bot.model.message.TextMessage
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.database.models.User
import me.konso.home_watchdog.entities.line.LineEvent
import java.io.File


suspend fun receiveMessageEvent(json: LineEvent){
    val logger = Store.Loggers.System
    val debugger = Store.Loggers.Debugger
    val client = Store.LINEBotClient
    val dao = Store.dao
    val talkStatus = mutableMapOf<String, Int>()

    try{
        // Get user from database
        val user = dao.getUserById(json.source?.userId?:"")?:return

        // Switch process by authorization
        if(!user.isAuthorized){
            challengeAuthorization(json, user)
            return
        }

        // Set default value
        if(!talkStatus.contains(user.id)){
            talkStatus[user.id] = 0
        }

        // Switch process by talking status
        when(talkStatus[user.id]?:0){
            1 -> {
                interactSettings(json, user)
            }
            0 -> {
                val message = json.message!!
                val text = message.text?:""

                if(text.startsWith("//")){
                    val tokens = text.split(" ")
                    when(tokens[0]){
                        "//settings" -> {
                            talkStatus[user.id] = 1
                        }
                        else -> {
                            client.replyMessage(
                                ReplyMessage(
                                    json.replyToken,
                                    TextMessage("Syntax Error")
                                )
                            )
                        }
                    }
                }else{
                    client.replyMessage(
                        ReplyMessage(
                            json.replyToken,
                            TextMessage("OK")
                        )
                    )
                }
            }
        }

    }catch (e: Exception){
        e.printStackTrace()
        logger.info("Execution Failed: {}", json)
    }
}

fun interactSettings(json: LineEvent, user: User){

}

suspend fun responseByChatGPT(msg: String, replyToken: String){
    val prompt = msg.trim()
    if(prompt.length > 256) return

    val req = CompletionRequest(
        model = ModelId("text-ada-001"),
        prompt = prompt,
        echo = true
    )

    val comp = Store.openai.completion(req)
    println(comp)


    Store.LINEBotClient.replyMessage(
        ReplyMessage(
            replyToken,
            TextMessage(comp.choices[0].text)
        )
    )
}

suspend fun challengeAuthorization(json: LineEvent, user: User){
    val logger = Store.Loggers.System
    val client = Store.LINEBotClient
    val dao = Store.dao

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
