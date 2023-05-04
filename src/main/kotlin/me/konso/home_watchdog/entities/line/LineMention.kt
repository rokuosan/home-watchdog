package me.konso.home_watchdog.entities.line

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LineMention(
    @SerialName("mentionees")
    val mention: List<LineMentionItem>
)

@Serializable
data class LineMentionItem(
    val index: Int,
    val length: Int,
    val type: String,
    val userId: String?
)