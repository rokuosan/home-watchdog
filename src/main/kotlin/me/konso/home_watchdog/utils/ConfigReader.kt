package me.konso.home_watchdog.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class ConfigReader(
    private val filename: String
) {
    private val file = File(filename)
    val text: String

    init {
        file.bufferedReader(Charsets.UTF_8).use {
            this.text = it.readText().trim()
        }
    }

    inline fun <reified T> getConfig() = Json.decodeFromString<T>(this.text)
}