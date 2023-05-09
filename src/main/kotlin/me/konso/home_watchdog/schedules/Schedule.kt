package me.konso.home_watchdog.schedules

interface Schedule {
    val name: String
    var every: Every
    var scheduler: Scheduler
}