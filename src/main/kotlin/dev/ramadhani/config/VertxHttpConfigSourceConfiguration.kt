package dev.ramadhani.config

import io.vertx.config.ConfigStoreOptions
import io.vertx.core.json.JsonObject
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperties
import java.io.ObjectInputFilter.Config


@ConfigProperties(prefix = "vertx-config-source")
data class VertxHttpConfigSourceConfiguration(val host: String, val port: Int, val path: String = "/", val headers: Map<String, String>) {

}