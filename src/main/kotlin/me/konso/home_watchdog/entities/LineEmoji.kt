package me.konso.home_watchdog.entities

import kotlinx.serialization.Serializable

@Serializable
data class LineEmoji(
    val index: Int,
    val length: Int,
    val productId: String,
    val emojiId: String
)
