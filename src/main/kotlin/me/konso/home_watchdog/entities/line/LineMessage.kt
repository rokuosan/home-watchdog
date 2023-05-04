package me.konso.home_watchdog.entities.line

import kotlinx.serialization.Serializable

@Serializable
data class LineMessage(
    val id: String,
    val type: String,
    val text: String? = null,
    val emojis: List<LineEmoji>? = null,
    val mention: LineMention? = null
)