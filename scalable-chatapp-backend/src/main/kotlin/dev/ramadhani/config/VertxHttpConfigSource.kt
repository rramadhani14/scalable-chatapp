package dev.ramadhani.config

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.Startup
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import jakarta.annotation.PostConstruct
import jakarta.enterprise.inject.Vetoed
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.eclipse.microprofile.config.spi.ConfigSource


//@Singleton
@Vetoed
class VertxHttpConfigSource (vertxHttpConfigSourceConfiguration: VertxHttpConfigSourceConfiguration, vertx: Vertx): ConfigSource {
    private val configStoreOptions = ConfigStoreOptions()
        .setType("http")
        .setConfig(
            JsonObject()
                .put("host", vertxHttpConfigSourceConfiguration.host())
                .put("port", vertxHttpConfigSourceConfiguration.port())
                .put("path", vertxHttpConfigSourceConfiguration.path())
                .put("headers", JsonObject.mapFrom(vertxHttpConfigSourceConfiguration.headers()))
        )
    private val configRetrieverOptions = ConfigRetrieverOptions()
        .addStore(configStoreOptions)
    private val configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions)
    private var currentConfig = mapOf<String, Any>()

//    @PostConstruct
//    fun init() {
//        configRetriever.listen {
//            Quarkus.asyncExit()
//        }
//    }

    override fun getPropertyNames(): MutableSet<String> {
        TODO("Not yet implemented")
    }

    override fun getValue(propertyName: String?): String {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

}