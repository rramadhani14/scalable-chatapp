package dev.ramadhani.user

import dev.ramadhani.room.Room
import dev.ramadhani.util.Identifiable
import io.quarkus.resteasy.reactive.links.RestLinkId

data class UserDTO(val username: String, val password: String)

data class User(@RestLinkId override val id: String, val username: String, val password: String, val rooms: List<Room> = listOf()): Identifiable