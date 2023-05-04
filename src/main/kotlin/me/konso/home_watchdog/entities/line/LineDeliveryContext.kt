package me.konso.home_watchdog.entities.line

import kotlinx.serialization.Serializable

@Serializable
data class LineDeliveryContext(
    val isRedelivery: Boolean
)