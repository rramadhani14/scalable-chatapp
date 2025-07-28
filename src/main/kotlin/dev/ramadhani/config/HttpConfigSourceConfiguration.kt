package dev.ramadhani.config

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperties


@ConfigProperties(prefix = "http-config-source")
data class HttpConfigSourceConfiguration(private val url: String, private val apiKey: String?)