package me.konso.home_watchdog.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.konso.home_watchdog.Store
import me.konso.home_watchdog.database.dao.DaoFacadeImpl
import me.konso.home_watchdog.database.models.StatusHistories
import me.konso.home_watchdog.database.models.Users
import me.konso.home_watchdog.entities.config.DatabaseConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object DatabaseFactory{
    fun init(){
        val logger = Store.Loggers.Debugger

        try {
            val jsonFormat = Json{prettyPrint=true}
            val file = File("database.json")

            // ファイルの存在を確認する。存在しない場合はデフォルトを出力
            if(!file.exists()){
                logger.debug("database.json does not exists.")
                val default = Store.Defaults.DATABASE_CONFIG
                val json = jsonFormat.encodeToString(default)

                logger.debug(json)
                logger.debug("Create database.json")
                file.bufferedWriter(Charsets.UTF_8).use {
                    it.append(json)
                    it.newLine()
                }
            }else {
                logger.debug("database.json was found!")
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
                SchemaUtils.create(StatusHistories)
            }

        }catch (e : Exception){
            logger.info("Connection to database failed. Please check the connection information.")
            try{
                CoroutineScope(context = Dispatchers.IO).launch {
                    logger.debug("Stopping the server...")
                    delay(1000)
                    val host = "localhost"
                    var port = "8080"
                    val shutdown = Store.shutdown
                    Store.config.propertyOrNull("port")?.apply {
                        port = this.toString()
                    }
                    val url = "http://$host:$port/${shutdown.replaceFirst("/", "")}"
                    logger.debug("Connect to ShutdownURL($url).")
                    val con = URL(url).openConnection() as HttpURLConnection
                    con.readTimeout = 5_000
                    con.connectTimeout = 5_000
                    con.connect()
                    con.inputStream.bufferedReader(Charsets.UTF_8).use{br ->
                        br.readLines().joinToString("")
                    }
                }
            }catch (ignored: Exception){
            }
        }
    }

    suspend fun <T> query(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}