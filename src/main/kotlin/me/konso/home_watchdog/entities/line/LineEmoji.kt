package me.konso.home_watchdog.entities.line

import kotlinx.serialization.Serializable

@Serializable
data class LineEmoji(
    val index: Int,
    val length: Int,
    val productId: String,
    val emojiId: String
)
