package dev.ramadhani.user

import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import java.util.UUID
import java.util.concurrent.ConcurrentLinkedDeque


@ApplicationScoped
class UserService(private val userRepository: UserPgRepository) {

    fun getUsers(): List<User> {
        return userRepository.getUsers()
    }

    fun getUser(username: String): User? {
        return userRepository.getUser(username)
    }

    fun getUserUni(username: String): Uni<User?> {
        return userRepository.getUserUni(username)
    }


    fun save(userDTO: UserDTO): User {
        val user = User(id = UUID.randomUUID().toString(), username = userDTO.username, password = BcryptUtil.bcryptHash(userDTO.password, 10))
        userRepository.saveNewUser(user)
        return user
    }

    fun updatePassword(user: User) {
        userRepository.updatePassword(user)
    }

    fun updateUsername(user: User) {
        userRepository.updateUsername(user)
    }

}