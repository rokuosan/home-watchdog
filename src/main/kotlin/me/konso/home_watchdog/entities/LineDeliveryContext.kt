package me.konso.home_watchdog.entities

import kotlinx.serialization.Serializable

@Serializable
data class LineDeliveryContext(
    val isRedelivery: Boolean
)