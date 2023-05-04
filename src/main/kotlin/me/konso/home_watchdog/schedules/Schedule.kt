package me.konso.home_watchdog.schedules

interface Schedule {
    val name: String
    var interval: Long
    var every: Every
    var scheduler: Scheduler
}