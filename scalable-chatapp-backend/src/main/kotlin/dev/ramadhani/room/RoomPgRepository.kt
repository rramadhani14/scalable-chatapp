package dev.ramadhani.room

import io.smallrye.mutiny.Uni
import io.vertx.mutiny.sqlclient.Pool
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.enterprise.context.ApplicationScoped
import java.time.Duration
import java.util.*
import java.util.function.Supplier


@ApplicationScoped
class RoomPgRepository(private val client: Pool) {
    fun getRooms(): Uni<List<Room>> {
        return client
            .preparedQuery("SELECT id, name FROM rooms")
            .execute()
            .onItem()
            .transform { it.map { row -> Room(row.getString("id"), row.getString("name"))}.toList() }
    }

    fun getRoom(id: String): Uni<Room?> {
        return client
            .preparedQuery("SELECT id, name FROM rooms WHERE id = $1")
            .execute(Tuple.of(id))
            .onItem()
            .transform { it.map { row -> Room(row.getString("id"), row.getString("name"))}.firstOrNull() }
    }

    fun save(room: Room): Uni<Room> {
        return client
            .preparedQuery("INSERT INTO rooms(id, name) VALUES ($1, $2) ON CONFLICT (id) DO UPDATE SET name = $2 WHERE id = $1 RETURNING id, name")
            .execute(Tuple.of(room.name, room.name))
            .onItem()
            .transform { it.map { row -> Room(row.getString("id"), row.getString("name"))}.first() }
    }

    fun delete(id: String): Uni<Any> {
        return client.preparedQuery("DELETE FROM rooms WHERE id = $1")
            .execute(Tuple.of(id))
            .chain(Supplier{ Uni.createFrom().nullItem() })
    }

    fun joinRoom(userId: String, roomId: String): Uni<Any> {
        return client.preparedQuery("INSERT INTO user_rooms(user_id, room_id) VALUES ($1, $2) ON CONFLICT (user_id, room_id) DO NOTHING")
            .execute(Tuple.of(userId, roomId))
            .chain(Supplier { Uni.createFrom().nullItem() })
    }

    fun leaveRoom(userId: String, roomId: String): Uni<Any> {
        return client.preparedQuery("DELETE FROM user_rooms WHERE user_id = $1 AND room_id = $2")
            .execute(Tuple.of(userId, roomId))
            .chain(Supplier { Uni.createFrom().nullItem() })
    }

    fun getUserRooms(userId: String): Uni<List<Room>> {
        return client.preparedQuery("SELECT room_id FROM user_rooms WHERE user_id = $1")
            .execute(Tuple.of(userId))
            .onItem()
            .transform { it.map { row -> Room(row.getString("room_id")!!, "")}.toList() }
    }
}