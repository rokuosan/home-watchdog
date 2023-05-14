package me.konso.home_watchdog.database.models

import org.jetbrains.exposed.sql.Table

data class StatusHistory(
    val time: String,
    val host: String,
    val status: Boolean
)

object StatusHistories: Table(){
    val time = varchar("time", 32).uniqueIndex()
    val host = varchar("host", 32)
    val status = bool("status").default(false)

    override val primaryKey = PrimaryKey(time)
    override val tableName: String
        get() = "status_histories"
}