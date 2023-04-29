package me.konso.home_watchdog

import com.linecorp.bot.client.LineMessagingClient
import io.ktor.server.application.*
import io.ktor.utils.io.charsets.Charsets
import me.konso.home_watchdog.database.DatabaseFactory
import me.konso.home_watchdog.plugins.configureHTTP
import me.konso.home_watchdog.plugins.configureLogging
import me.konso.home_watchdog.plugins.configureRouting
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import java.io.File

fun main(args: Array<String>): Unit=
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    initDotEnvParam()
    DatabaseFactory.init()
    initLINEBot()

    configureHTTP()
    configureLogging()
    configureRouting()
}

/**
 * LINE Bot SDK で利用するボットクライアントの初期化処理を行う関数
 */
fun initLINEBot(){
    val token = System.getProperty("CHANNEL_ACCESS_TOKEN")

    val client = LineMessagingClient
        .builder(token)
        .okHttpClientBuilder(OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level=HttpLoggingInterceptor.Level.NONE
            }), true)
        .build()

    val l = LoggerFactory.getLogger("com.linecorp.bot.client.wire")
    HttpLoggingInterceptor(l::info).level = HttpLoggingInterceptor.Level.NONE

    Store.LINEBotClient = client
}


/**
 * .envファイルを読み取りシステムプロパティに定義する関数
 * ファイルフォーマットは``KEY=VALUE``として読み取ります。
 */
fun Application.initDotEnvParam(){
    Store.config = environment.config
    val logger = Store.Loggers.System

    logger.info("Reading .env file...")

    val file = File(".env")
    if(!file.exists()){
        logger.info("Not found .env file on ${File("").absoluteFile}")
        return
    }

    try{
        file.bufferedReader(charset = Charsets.UTF_8).readLines().forEach { line ->
            val record = line.split("=", limit = 2)
            logger.debug("Read: $line")

            if(record.size == 2){
                val key = record[0].replace(" ", "")
                val value = record[1]
                System.setProperty(key, value)
                logger.debug("Set: $key=$value")
            }
        }
        logger.info("Complete reading .env file.")
    }catch(e: Exception){
        logger.debug(e.stackTraceToString())
        logger.info("Failed to read .env file.")
    }
}
