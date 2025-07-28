package dev.ramadhani.chat

import dev.ramadhani.util.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import java.time.Instant
import java.util.UUID


@ApplicationScoped
class ChatService(private val chatRepository: ChatPgRepository) {

    fun getPagedMessagesByRoom(id: String, cursor: Instant?, limit: Int): Uni<Page<ChatMessage, Instant>> {
        return chatRepository.getPagedMessagesByRoom(id, cursor ?: Instant.now(), limit)
    }

    fun save(messageDTO: ChatMessageDTO, userId: String): Uni<ChatMessage> {
        val message = ChatMessage(id = UUID.randomUUID().toString(), message = messageDTO.message, room = messageDTO.room, sender = userId, timestamp = Instant.now())
        return chatRepository.save(message)
    }
}