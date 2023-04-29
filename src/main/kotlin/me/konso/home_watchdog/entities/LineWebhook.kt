package me.konso.home_watchdog.entities

import kotlinx.serialization.Serializable

@Serializable
data class LineWebhook(
    val destination: String,
    val events: List<LineEvent>
)