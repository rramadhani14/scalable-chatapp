package dev.ramadhani.user

import dev.ramadhani.room.RoomService
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import java.util.UUID
import java.util.concurrent.ConcurrentLinkedDeque


@ApplicationScoped
class UserService(private val userRepository: UserPgRepository, private val roomService: RoomService) {

    fun getUsers(): Uni<List<User>> {
        return userRepository.getUsers()
    }

    fun getUser(username: String): Uni<User?> {
        return userRepository.getUser(username)
    }

    fun save(userDTO: UserDTO): Uni<User> {
        val user = User(id = UUID.randomUUID().toString(), username = userDTO.username, password = BcryptUtil.bcryptHash(userDTO.password, 10))
        return userRepository.saveNewUser(user)
    }

    fun updatePassword(user: User): Uni<Any> {
        return userRepository.updatePassword(user)
    }

    fun updateUsername(user: User): Uni<Any> {
        return userRepository.updateUsername(user)
    }
    fun getCurrentUser(username: String): Uni<User?> {
        return userRepository.getUser(username)
            .chain { user ->
                if(user != null) {
                    return@chain roomService.getUserRooms(user.id)
                        .onItem()
                        .transform { rooms ->
                            User(user.id, user.username, "", rooms)
                        }
                }
                return@chain Uni.createFrom().nullItem<User>()
            }

    }

}