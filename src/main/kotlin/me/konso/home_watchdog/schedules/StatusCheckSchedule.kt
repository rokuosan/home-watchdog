package me.konso.home_watchdog.schedules

import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.TextMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.config.TargetsConfig
import me.konso.home_watchdog.utils.Ping
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.TimeUnit

class StatusCheckSchedule: Schedule {
    override val name: String = this::class.java.canonicalName
    private val logger = LoggerFactory.getLogger(this::class.java)
    override var interval: Long = Store.Defaults.TARGETS_CONFIG.interval
    override var scheduler: Scheduler = Scheduler {}
    override var every: Every = Every(Store.Defaults.TARGETS_CONFIG.interval, TimeUnit.MILLISECONDS)

    init {
        try{
            val f = File("targets.json")
            if(!f.exists()){
                val format = Json { prettyPrint = true }
                f.bufferedWriter(Charsets.UTF_8).use {
                    it.write(format.encodeToString(Store.Defaults.TARGETS_CONFIG))
                    it.newLine()
                    it.flush()
                }
            }

            val str = f.bufferedReader(Charsets.UTF_8).readLines().joinToString("").trim()
            val config = Json.decodeFromString<TargetsConfig>(str)
            this.interval = config.interval

            for (server in config.servers){
                Store.ServerStatus[server] = false
            }

            this.scheduler = Scheduler(kotlinx.coroutines.Runnable {
                for (server in config.servers){
                    CoroutineScope(Dispatchers.IO).launch {
                        val p = Ping(server, config.timeout)
                        val prev = Store.ServerStatus[server]?:false
                        val res = p.ping()
                        logger.debug("HOST: $server => $res")

                        Store.ServerStatus[server] = res

                        if(prev && !res){
                            // Previous is true(= Server is up)
                            // But now is false(= Server is down)
                            sendConnectionAlert(server, true)
                        }
                        if(!prev && res){
                            sendConnectionAlert(server, false)
                        }
                    }
                }
            })

            this.every = Every(config.interval, TimeUnit.MILLISECONDS)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}

private suspend fun sendConnectionAlert(server: String, goneDown: Boolean){
    val msg = if(goneDown){
        "🔴ALERT\nHost(${server}) has been down."
    }else{
        "🟢ALERT\nHost(${server}) has been up."
    }

    val users = Store.dao.getAllUsers()

    for(user in users){
        if(user.notificationLevel < 1) continue
        val push = PushMessage(user.id, TextMessage(msg))

        try{
            val res = Store.LINEBotClient.pushMessage(push).get()
            println("Alert was sent to ${user.id} [${res.requestId}]")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
