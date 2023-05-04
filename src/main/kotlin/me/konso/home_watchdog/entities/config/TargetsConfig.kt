package me.konso.home_watchdog.entities.config

import kotlinx.serialization.Serializable

@Serializable
data class TargetsConfig(
    val servers: List<String>,
    val interval: Long,
    val timeout: Int
)