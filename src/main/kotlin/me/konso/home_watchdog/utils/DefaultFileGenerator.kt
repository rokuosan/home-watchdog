package me.konso.home_watchdog.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.charset.Charset

class DefaultFileGenerator(
    private val filename: String,
    val charset: Charset = Charsets.UTF_8
) {
    val file = File(this.filename)

    inline fun <reified T>create(value: T, overwrite: Boolean = false){
        val format = Json { prettyPrint = true }
        val text = format.encodeToString(value)

        if(overwrite || !this.file.exists()){
            file.writeText(text, this.charset)
        }
    }
}