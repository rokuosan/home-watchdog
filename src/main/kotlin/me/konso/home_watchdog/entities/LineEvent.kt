package me.konso.home_watchdog.entities

import kotlinx.serialization.Serializable
import me.konso.home_watchdog.events.execute

@Serializable
data class LineEvent(
    val type: String,
    val mode: String,
    val timestamp: Long,
    val webhookEventId: String,
    val deliveryContext: LineDeliveryContext,
    val replyToken: String? = null,
    val source: LineSource? = null,
    val message: LineMessage? = null
){
    suspend fun executeEvent() = this.execute()
}
