package me.konso.home_watchdog.database.models

import org.jetbrains.exposed.sql.Table

data class User(
    val id: Int,
    val nickname: String,
    val identifier: String,
    val isAuthorized: Boolean,
    val permissionLevel: Int,
)

object Users: Table(){
    val id = integer("id").autoIncrement()
    val nickname = varchar("nickname", 256)
    val identifier = varchar("identifier", 64)
    val isAuthorized = bool("is_authorized").default(false)
    val permissionLevel = integer("permission_level").default(0)

    override val primaryKey = PrimaryKey(id)
}
