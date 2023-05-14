package me.konso.home_watchdog.schedules

import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.TextMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.entities.config.TargetsConfig
import me.konso.home_watchdog.utils.ConfigReader
import me.konso.home_watchdog.utils.DefaultFileGenerator
import me.konso.home_watchdog.utils.Ping
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class DailyStatusReportSchedule: Schedule {
    override val name: String = this::class.java.canonicalName
    override var every: Every = Every(Store.Defaults.TARGETS_CONFIG.interval, TimeUnit.MILLISECONDS)
    override var scheduler: Scheduler = Scheduler {}

    private var time: LocalDateTime

    init {
        time = LocalDateTime.now().minusDays(1)

        val config: TargetsConfig
        "targets.json".apply {
            DefaultFileGenerator(this).create(Store.Defaults.TARGETS_CONFIG, overwrite = false)
            config = ConfigReader(this).getConfig<TargetsConfig>()
        }

        this.every = Every(1, TimeUnit.MINUTES)

        this.scheduler = Scheduler(kotlinx.coroutines.Runnable {
            val now = LocalDateTime.now()
            if(now.hour != 7) return@Runnable
            if(time.dayOfYear == now.dayOfYear) return@Runnable
            time = LocalDateTime.now()

            val result = mutableMapOf<String, Boolean>()
            config.servers.forEach { server ->
                val res = Ping(server, config.timeout).ping()
                result[server] = res
            }

            val msgs = mutableListOf("Daily Report")
            result.forEach { (k, v) ->
                if(v){
                    msgs.add("🟢 $k is UP")
                }else{
                    msgs.add("🔴 $k is DOWN")
                }
            }
            val msg = msgs.joinToString("\n")

            CoroutineScope(Dispatchers.IO).launch {
                val users = Store.dao.getAllUsers()
                for(user in users){
                    if(user.notificationLevel < 1) continue
                    val p = PushMessage(user.id, TextMessage(msg))

                    try{
                        Store.LINEBotClient.pushMessage(p).get()
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        })

    }
}
