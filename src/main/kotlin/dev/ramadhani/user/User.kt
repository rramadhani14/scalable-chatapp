package dev.ramadhani.user

import dev.ramadhani.util.Identifiable
import io.quarkus.resteasy.reactive.links.RestLinkId

data class UserDTO(val username: String, val password: String)

data class User(@RestLinkId override val id: String, val username: String, val password: String, val rooms: List<String> = listOf()): Identifiable