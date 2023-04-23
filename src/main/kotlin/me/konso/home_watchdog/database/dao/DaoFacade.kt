package me.konso.home_watchdog.database.dao

import me.konso.home_watchdog.database.models.User

interface DaoFacade {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: Int): User?
    suspend fun addUser(nickname: String, identifier: String): User?
    suspend fun changePermissionLevel(id: Int, level: Int): User?
    suspend fun authorize(id: Int, authorized: Boolean): User?
    suspend fun deleteUser(id: Int): Boolean
}