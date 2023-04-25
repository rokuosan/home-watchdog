package me.konso.home_watchdog.database

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.database.dao.DaoFacadeImpl
import me.konso.home_watchdog.database.models.Users
import me.konso.home_watchdog.entities.DatabaseConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseFactory{
    fun init(){
        val logger = Store.Loggers.Debugger

        try {
            val jsonFormat = Json{prettyPrint=true}
            val file = File("database.json")

            // ファイルの存在を確認する。存在しない場合はデフォルトを出力
            if(!file.exists()){
                logger.debug("database.json がありませんでした。")
                val default = Store.Defaults.DATABASE_CONFIG
                val json = jsonFormat.encodeToString(default)

                logger.debug(json)
                logger.debug("database.jsonを作成します。")
                file.bufferedWriter(Charsets.UTF_8).use {
                    it.append(json)
                    it.newLine()
                }
            }else {
                logger.debug("database.json を発見しました。")
            }

            // ファイル読み取り
            val rawString = file.bufferedReader(Charsets.UTF_8)
                .readLines()
                .joinToString("")
            val config = jsonFormat.decodeFromString<DatabaseConfig>(rawString)

            // DB 接続
            val database = Database.connect(
                driver = config.databaseDriver,
                url = config.databaseUrl,
                user = config.databaseUser,
                password = config.databasePass
            )

            // DAO
            DaoFacadeImpl().apply {
                Store.dao = this
            }

            // 初期処理
            transaction(database) {
                SchemaUtils.create(Users)
            }

        }catch (e : Exception){
            logger.info("DB接続に失敗しました。接続情報を確認してください。")
            e.printStackTrace()
        }
    }

    suspend fun <T> query(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}