package me.konso.home_watchdog.database.models

import org.jetbrains.exposed.sql.Table

data class User(
    val id: String,
    val isAuthorized: Boolean,
    val permissionLevel: Int,
)

object Users: Table(){
    val id = varchar("id", 64)
    val isAuthorized = bool("is_authorized").default(false)
    val permissionLevel = integer("permission_level").default(0)

    override val primaryKey = PrimaryKey(id)
}
