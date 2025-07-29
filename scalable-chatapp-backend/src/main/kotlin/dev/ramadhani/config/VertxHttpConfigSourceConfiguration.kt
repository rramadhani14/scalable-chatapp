package dev.ramadhani.config


import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "vertx-config-source")
interface VertxHttpConfigSourceConfiguration {
    fun host(): String
    fun port(): Int
    fun path() = "/"
    fun headers(): Map<String, String> = mapOf()
}