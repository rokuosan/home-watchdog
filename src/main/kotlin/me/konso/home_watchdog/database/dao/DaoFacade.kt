package me.konso.home_watchdog.database.dao

import me.konso.home_watchdog.database.models.StatusHistory
import me.konso.home_watchdog.database.models.User

interface DaoFacade {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: String): User?
    suspend fun addUser(id: String): User?
    suspend fun changePermissionLevel(id: String, level: Int): User?
    suspend fun authorize(id: String, authorized: Boolean): User?
    suspend fun deleteUser(id: String): Boolean

    suspend fun getAllHistories(): List<StatusHistory>
    suspend fun getHistoriesByHost(host: String): List<StatusHistory>
    suspend fun addHistory(host: String, status: Boolean): StatusHistory?
}