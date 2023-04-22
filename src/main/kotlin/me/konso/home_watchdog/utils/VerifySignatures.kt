package me.konso.home_watchdog.utils

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun PipelineContext<Unit, ApplicationCall>.verifySignatures(body: String): Boolean{
    return try{
        val secret = System.getProperty("CHANNEL_SECRET")
        val hmac = "HmacSHA256"
        val key = SecretKeySpec(secret.toByteArray(), hmac)
        val mac = Mac.getInstance(hmac)
        val sig = this.call.request.headers["x-line-signature"]?:return false

        mac.init(key)

        val src = body.toByteArray(Charsets.UTF_8)
        val result = Base64.getEncoder().encodeToString(mac.doFinal(src))

        sig == result
    }catch(e: Exception){
        false
    }
}
