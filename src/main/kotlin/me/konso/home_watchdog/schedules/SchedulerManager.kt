package me.konso.home_watchdog.schedules

import org.slf4j.LoggerFactory


class SchedulerManager {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var scheduleList: List<Schedule> = emptyList()

    companion object {
        fun init(): SchedulerManager{
            val sm = SchedulerManager()

            val ss = StatusCheckSchedule()
            sm.logger.debug("Registering ${ss.name}")
            sm.addSchedule(ss)

            sm.registerAll()
            return sm
        }
    }

    fun registerAll(){
        scheduleList.forEach{
            it.scheduler.scheduleExecution(it.every)
            logger.debug("Registered: ${it.name}")
        }
    }

    fun stopAll(){
        scheduleList.forEach {
            it.scheduler.stop()
        }
    }

    fun addSchedule(schedule: Schedule){
        val ml = this.scheduleList.toMutableList()
        ml.add(schedule)
        this.scheduleList = ml.toList()
    }
}
