package me.konso.home_watchdog.database.dao

import me.konso.home_watchdog.database.models.User
import me.konso.home_watchdog.database.models.Users
import me.konso.home_watchdog.database.DatabaseFactory.query
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DaoFacadeImpl: DaoFacade{
    private fun toUser(row: ResultRow) = User(
        id = row[Users.id],
        nickname = row[Users.nickname],
        identifier = row[Users.identifier],
        isAuthorized = row[Users.isAuthorized],
        permissionLevel = row[Users.permissionLevel]
    )

    override suspend fun getAllUsers(): List<User> = query {
        Users.selectAll().map(::toUser)
    }

    override suspend fun getUserById(id: Int): User? = query {
        Users.select{ Users.id eq id }.map(::toUser).singleOrNull()
    }

    override suspend fun addUser(nickname: String, identifier: String): User? = query {
        val ins = Users.insert {
            it[Users.nickname] = nickname
            it[Users.identifier] = identifier
            it[isAuthorized] = false
            it[permissionLevel] = 0
        }
        ins.resultedValues?.singleOrNull()?.let(::toUser)
    }

    override suspend fun changePermissionLevel(id: Int, level: Int): User? = query {
        if(Users.update({Users.id eq id}){
            it[permissionLevel] = level
        } > 0){
            getUserById(id)
        }else{
            null
        }
    }

    override suspend fun authorize(id: Int, authorized: Boolean): User? = query {
        if(Users.update({Users.id eq id}){
                it[isAuthorized] = authorized
            } > 0){
            getUserById(id)
        }else{
            null
        }
    }

    override suspend fun deleteUser(id: Int): Boolean = query {
        Users.deleteWhere { Users.id eq id } > 0
    }

}