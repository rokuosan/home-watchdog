package me.konso.home_watchdog.database

import kotlinx.coroutines.Dispatchers
import me.konso.home_watchdog.database.models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory{
    fun init(){
        val driver = "org.postgresql.Driver"
        val url = "jdbc:postgresql://10.10.10.101:5432/postgres"
        val database = Database.connect(url, driver, user = "postgres", password = "sample")

        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> query(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}