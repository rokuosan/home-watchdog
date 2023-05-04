package me.konso.home_watchdog.events

import me.konso.home_watchdog.entities.line.LineEvent

suspend fun LineEvent.execute(){
    when(type){
        "follow" -> { followEvent(this)  }
        "unfollow" -> { unfollowEvent(this) }
        "message" -> { receiveMessageEvent(this) }
        else -> {}
    }
}