package dev.ramadhani.room

import dev.ramadhani.util.Identifiable
import io.quarkus.resteasy.reactive.links.RestLinkId


data class RoomDTO(val name: String)
data class Room(@RestLinkId override val id: String, val name: String): Identifiable
