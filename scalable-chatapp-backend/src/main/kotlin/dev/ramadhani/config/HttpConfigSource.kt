package dev.ramadhani.config

import io.quarkus.runtime.Startup
import jakarta.enterprise.inject.Vetoed
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.eclipse.microprofile.config.spi.ConfigSource

@Vetoed
@Startup
class HttpConfigSource @Inject constructor(val httpConfigSourceConfiguration: HttpConfigSourceConfiguration): ConfigSource {
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