package me.konso.home_watchdog.entities

import kotlinx.serialization.Serializable

@Serializable
data class LineSource(
    val type: String,
    val userId: String? = null,
    val groupId: String? = null,
    val roomId: String? = null
)