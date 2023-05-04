package me.konso.home_watchdog.database.dao

import me.konso.home_watchdog.database.models.User
import me.konso.home_watchdog.database.models.Users
import me.konso.home_watchdog.database.DatabaseFactory.query
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DaoFacadeImpl: DaoFacade{
    private fun toUser(row: ResultRow) = User(
        id = row[Users.id],
        isAuthorized = row[Users.isAuthorized],
        permissionLevel = row[Users.permissionLevel],
        notificationLevel = row[Users.notificationLevel]
    )

    override suspend fun getAllUsers(): List<User> = query {
        Users.selectAll().map(::toUser)
    }

    override suspend fun getUserById(id: String): User? = query {
        Users.select{ Users.id eq id }.map(::toUser).singleOrNull()
    }

    override suspend fun addUser(id: String): User? = query {
        val ins = Users.insert {
            it[Users.id] = id
            it[isAuthorized] = false
            it[permissionLevel] = 0
        }
        ins.resultedValues?.singleOrNull()?.let(::toUser)
    }

    override suspend fun changePermissionLevel(id: String, level: Int): User? = query {
        if(Users.update({Users.id eq id}){
            it[permissionLevel] = level
        } > 0){
            getUserById(id)
        }else{
            null
        }
    }

    override suspend fun authorize(id: String, authorized: Boolean): User? = query {
        if(Users.update({Users.id eq id}){
                it[isAuthorized] = authorized
            } > 0){
            getUserById(id)
        }else{
            null
        }
    }

    override suspend fun deleteUser(id: String): Boolean = query {
        Users.deleteWhere { Users.id eq id } > 0
    }

}