package dev.ramadhani.room

import io.vertx.mutiny.sqlclient.Pool
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.enterprise.context.ApplicationScoped
import java.time.Duration
import java.util.*

//TODO: Change to suspendable version
@ApplicationScoped
class RoomPgRepository(private val client: Pool) {
    fun getRooms(): List<Room> {
        return client
            .preparedQuery("SELECT id, name FROM rooms")
            .execute()
            .onItem()
            .transform { it.map { row -> Room(row.getString("id"), row.getString("name"))}.toList() }
            .await()
            .atMost(Duration.ofSeconds(5))
    }

    fun getRoom(id: String): List<Room> {
        return client
            .preparedQuery("SELECT id, name FROM rooms WHERE id = $1")
            .execute(Tuple.of(id))
            .onItem()
            .transform { it.map { row -> Room(row.getString("id"), row.getString("name"))}.toList() }
            .await()
            .atMost(Duration.ofSeconds(5))
    }

    fun save(room: Room) {
        client
            .preparedQuery("INSERT INTO rooms(id, name) VALUES ($1, $2) ON CONFLICT (id) DO UPDATE SET name = $2 WHERE id = $1")
            .execute(Tuple.of(room.name, room.name))
            .await()
            .atMost(Duration.ofSeconds(5))
    }

    fun delete(id: String) {
        client.preparedQuery("DELETE FROM rooms WHERE id = $1")
            .execute(Tuple.of(id))
            .await()
            .atMost(Duration.ofSeconds(5))
    }

    fun joinRoom(userId: String, roomId: String) {
        client.preparedQuery("INSERT INTO user_rooms(user_id, room_id) VALUES ($1, $2) ON CONFLICT (user_id, room_id) DO NOTHING")
            .execute(Tuple.of(userId, roomId))
            .await()
            .atMost(Duration.ofSeconds(5))
    }

    fun leaveRoom(userId: String, roomId: String) {
        client.preparedQuery("DELETE FROM user_rooms WHERE user_id = $1 AND room_id = $2")
            .execute(Tuple.of(userId, roomId))
            .await()
            .atMost(Duration.ofSeconds(5))
    }

    fun getUserRooms(userId: String): List<Room> {
        return client.preparedQuery("SELECT room_id FROM user_rooms WHERE user_id = $1")
            .execute(Tuple.of(userId))
            .onItem()
            .transform { it.map { row -> Room(row.getString("room_id")!!, "")}.toList() }
            .await()
            .atMost(Duration.ofSeconds(5))
    }
}