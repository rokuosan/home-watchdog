package me.konso.home_watchdog.entities

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

enum class LineDeliveryContextProperty(
    val key: String
){
    IS_REDELIVERY("isRedelivery")
}

enum class LineSourceUser(val key: String){
    TYPE_STRING("type"),
    USER_ID("userId")
}
