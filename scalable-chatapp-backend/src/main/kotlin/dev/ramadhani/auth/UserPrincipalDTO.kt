package dev.ramadhani.auth

import java.security.Principal

data class UserPrincipalDTO(val id: String, val username: String): Principal {
    override fun getName(): String {
        return "$id:$username"
    }
}