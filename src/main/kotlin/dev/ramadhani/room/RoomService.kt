package dev.ramadhani.room

import jakarta.enterprise.context.ApplicationScoped
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque


@ApplicationScoped
class RoomService(private val roomRepository: RoomPgRepository) {

    fun getRooms(): List<Room> {
        return roomRepository.getRooms()
    }

    fun getRoom(id: String): Room? {
        return roomRepository.getRoom(id).getOrNull(0)
    }

    fun save(room: RoomDTO): Room {
        val id = UUID.randomUUID().toString()
        val room = Room(id, room.name)
        roomRepository.save(room)
        return room
    }

    fun delete(id: String) {
        roomRepository.delete(id)
    }

    fun joinRoom(userId: String, roomId: String) {
        roomRepository.joinRoom(userId, roomId)
    }

    fun leaveRoom(userId: String, roomId: String) {
        roomRepository.leaveRoom(userId, roomId)
    }

    fun getUserRooms(userId: String): List<Room> {
        return roomRepository.getUserRooms(userId)
    }
}