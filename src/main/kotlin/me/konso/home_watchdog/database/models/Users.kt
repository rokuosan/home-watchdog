package me.konso.home_watchdog.database.models

import org.jetbrains.exposed.sql.Table

data class User(
    val id: String,
    val isAuthorized: Boolean = false,
    val permissionLevel: Int = 0,
    val notificationLevel: Int = 0
)

object Users: Table(){
    val id = varchar("id", 64)
    val isAuthorized = bool("is_authorized").default(false)
    val permissionLevel = integer("permission_level").default(0)
    val notificationLevel = integer("notification_level").default(0)

    override val primaryKey = PrimaryKey(id)
}
