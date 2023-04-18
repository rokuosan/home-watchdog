package me.konso.home_watchdog

import io.ktor.server.application.*
import io.ktor.utils.io.charsets.Charsets
import me.konso.home_watchdog.plugins.configureLogging
import me.konso.home_watchdog.plugins.configureRouting
import java.io.File

fun main(args: Array<String>): Unit=
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    setEnvParam()

    configureLogging()
    configureRouting()
}


/**
 * .envファイルを読み取りシステムプロパティに定義する関数
 * ファイルフォーマットは``KEY=VALUE``として読み取ります。
 */
fun setEnvParam(){
    val logger = Store.SystemLogger

    logger.info("Reading .env file...")

    val file = File(".env")
    if(!file.exists()){
        logger.info("Not found .env file on ${File("").absoluteFile}")
        return
    }

    try{
        file.bufferedReader(charset = Charsets.UTF_8).readLines().forEach { line ->
            val record = line.split("=")
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
