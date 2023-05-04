package me.konso.home_watchdog.utils

import org.slf4j.LoggerFactory
import java.net.InetAddress

class Ping(
    private val host: String,
    private val timeout: Int = 5_000
){
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun ping(): Boolean{
        logger.debug("Challenging Ping to {}. Timeout is {} ms", host, timeout)
        val addr = InetAddress.getByName(this.host)
        logger.debug(addr.toString())
        return addr.isReachable(timeout)
    }
}