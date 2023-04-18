package me.konso.home_watchdog.utils

import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun verifySignatures(signature: String, body: String): Boolean{
    return try{
        val secret = System.getProperty("CHANNEL_SECRET")
        val hmac = "HmacSHA256"
        val key = SecretKeySpec(secret.toByteArray(), hmac)
        val mac = Mac.getInstance(hmac)

        mac.init(key)

        val src = body.toByteArray(Charsets.UTF_8)
        val result = Base64.getEncoder().encodeToString(mac.doFinal(src))

        signature == result
    }catch(e: Exception){
        false
    }
}
