package dev.ramadhani.user

import dev.ramadhani.util.Identifiable

data class UserDTO(val username: String, val password: String)

data class User(override val id: String, val username: String, val password: String, val rooms: List<String> = listOf()): Identifiable