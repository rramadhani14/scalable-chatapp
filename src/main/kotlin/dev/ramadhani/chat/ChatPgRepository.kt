package dev.ramadhani.chat

import dev.ramadhani.util.Page
import io.vertx.mutiny.sqlclient.Pool
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.enterprise.context.ApplicationScoped
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

//TODO: Change to suspendable version
@ApplicationScoped
class ChatPgRepository(private val client: Pool) {
    fun save(message: ChatMessage) {
        client
            .preparedQuery("INSERT INTO chat_messages (id, sender, room, message, timestamp) VALUES ($1, $2, $3, $4, $5)")
            .execute(Tuple.of(message.id, message.sender, message.room, message.message, message.timestamp.atOffset(
                ZoneOffset.UTC)))
            .await().atMost(Duration.ofSeconds(5))
    }

    fun getPagedMessagesByRoom(id: String, timestamp: Instant, limit: Int): Page<ChatMessage, Instant> {
        val res = client
            .preparedQuery("SELECT id, sender, room, message, timestamp FROM chat_messages WHERE timestamp < $1 ORDER BY timestamp DESC LIMIT $2")
            .execute(Tuple.of(timestamp.atOffset(ZoneOffset.UTC), limit + 1))
            .onItem()
            .transform { it.map { row -> ChatMessage(row.getString("id"), row.getString("sender"), row.getString("room"), row.getString("message"), row.getOffsetDateTime("timestamp").toInstant()) }.toList() }
            .await().atMost(Duration.ofSeconds(5))
        return Page(res.take(limit), limit.toUInt(), res.size > limit, timestamp)
    }
}