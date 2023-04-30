package me.konso.home_watchdog.utils

import kotlinx.coroutines.Runnable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Scheduler(
    private val task: Runnable
) {
    private val executor = Executors.newScheduledThreadPool(1)

    fun scheduleExecution(every: Every, initialDelay: Long = every.n){
        val tw = Runnable{
            task.run()
        }

        executor.scheduleWithFixedDelay(tw, initialDelay, every.n, every.unit)
    }

    fun stop(){
        try{
            executor.shutdown()
            executor.awaitTermination(1, TimeUnit.HOURS)
        }catch (ignored: Exception){
        }
    }
}

data class Every(
    val n: Long,
    val unit: TimeUnit
)