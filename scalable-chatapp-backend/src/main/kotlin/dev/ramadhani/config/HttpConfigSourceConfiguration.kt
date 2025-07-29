package dev.ramadhani.config

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "http-config-source")
interface HttpConfigSourceConfiguration {
    fun url(): String
    fun apiKey(): String
}