package me.konso.home_watchdog.entities.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PythonConfig(
    @SerialName("runtime")
    val runtime: String,
)