package dev.ramadhani.room

import dev.ramadhani.util.Identifiable


data class RoomDTO(val name: String)
data class Room(override val id: String, val name: String): Identifiable
