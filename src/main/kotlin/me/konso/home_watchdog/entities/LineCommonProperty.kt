package me.konso.home_watchdog.entities

import kotlinx.serialization.SerialName

enum class LineCommonProperty(
    val key: String
) {
    TYPE_STRING("type"),
    MODE_STRING("mode"),
    TIMESTAMP_NUMBER("timestamp"),
    SOURCE_OBJECT("source"),
    WEBHOOK_EVENT_ID("webhookEventId"),
    DELIVERY_CONTEXT_OBJECT("deliveryContext"),
}


@kotlinx.serialization.Serializable
data class WebhookObject(
    @SerialName("destination")
    val destination: String,

    @SerialName("events")
    val events: List<EventObject>
)

@kotlinx.serialization.Serializable
data class EventObject(
    val type: String,
    val mode: String,
    val timestamp: Long,
    val webhookEventId: String,
    val deliveryContext: DeliveryContext,
    val source: SourceObject?,
    val message: MessageObject?,
    val replyToken: String?
)


@kotlinx.serialization.Serializable
data class MessageObject(
    val id: String,
    val type: String,
    val text: String? = null,
    val emojis: List<EmojiObject>? = null,
    val mention: MentionObject? = null
)

@kotlinx.serialization.Serializable
data class MentionObject(
    val mentionees: List<MentioneesItem>
)

@kotlinx.serialization.Serializable
data class MentioneesItem(
    val index: Int,
    val length: Int,
    val type: String,
    val userId: String?
)

@kotlinx.serialization.Serializable
data class EmojiObject(
    val index: Int,
    val length: Int,
    val productId: String,
    val emojiId: String,
)

@kotlinx.serialization.Serializable
data class DeliveryContext(
    val isRedelivery: Boolean
)

@kotlinx.serialization.Serializable
data class SourceObject(
    val type: String,
    val userId: String?,
    val groupId: String?,
    val roomId: String?
)

enum class SourceType{
    USER,
    GROUP,
    ROOM
}

enum class EventMode{
    ACTIVE,
    STANDBY
}

