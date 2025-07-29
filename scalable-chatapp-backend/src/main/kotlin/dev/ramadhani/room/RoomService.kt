package dev.ramadhani.room

import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque


@ApplicationScoped
class RoomService(private val roomRepository: RoomPgRepository) {

    fun getRooms(): Uni<List<Room>> {
        return roomRepository.getRooms()
    }

    fun getRoom(id: String): Uni<Room?> {
        return roomRepository.getRoom(id)
    }

    fun save(room: RoomDTO): Uni<Room> {
        val id = UUID.randomUUID().toString()
        val room = Room(id, room.name)
        return roomRepository.save(room)
    }

    fun delete(id: String): Uni<Any> {
        return roomRepository.delete(id)
    }

    fun joinRoom(userId: String, roomId: String): Uni<Any> {
        return roomRepository.joinRoom(userId, roomId)
    }

    fun leaveRoom(userId: String, roomId: String): Uni<Any> {
        return roomRepository.leaveRoom(userId, roomId)
    }

    fun getUserRooms(userId: String): Uni<List<Room>> {
        return roomRepository.getUserRooms(userId)
    }
}