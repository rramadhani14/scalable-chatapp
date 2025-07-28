package dev.ramadhani.config

import io.quarkus.runtime.Startup
import jakarta.inject.Singleton
import org.eclipse.microprofile.config.spi.ConfigSource

@Singleton
@Startup
class HttpConfigSource(private val httpConfigSourceConfiguration: HttpConfigSourceConfiguration): ConfigSource {
    private var currentConfig = mapOf<String, Any>()
    fun loadConfig() {
        TODO()
    }

    override fun getPropertyNames(): MutableSet<String> {
        TODO()
    }

    override fun getValue(propertyName: String?): String {
        TODO()
    }

    override fun getName(): String {
        TODO()
    }
}