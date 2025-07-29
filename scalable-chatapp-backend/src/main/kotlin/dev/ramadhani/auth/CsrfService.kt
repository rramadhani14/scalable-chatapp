package dev.ramadhani.auth

import jakarta.enterprise.context.ApplicationScoped
import org.cache2k.Cache2kBuilder
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

@ApplicationScoped
class CsrfService {
    private val tokenStore = Cache2kBuilder
        .of(String::class.java, String::class.java)
        .expireAfterWrite(Duration.ofMinutes(30))
        .build()

    fun createToken(username: String): String {
        val token = UUID.randomUUID().toString()
        tokenStore.put(username, token)
        return token
    }

    fun getToken(username: String): String {
        val res = tokenStore.get(username)
        if (res != null) {
            tokenStore.invoke(username){ entry ->
                if(entry.exists()) entry.setExpiryTime(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30))
            }
            return res
        }

        return createToken(username)
    }

    fun deleteToken(username: String) {
        tokenStore.remove(username)
    }
}