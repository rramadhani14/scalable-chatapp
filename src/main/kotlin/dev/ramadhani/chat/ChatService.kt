package dev.ramadhani.chat

import dev.ramadhani.util.Page
import jakarta.enterprise.context.ApplicationScoped
import java.time.Instant
import java.util.UUID


@ApplicationScoped
class ChatService(private val chatRepository: ChatPgRepository) {

    fun getPagedMessagesByRoom(id: String, cursor: Instant?, limit: Int): Page<ChatMessage, Instant> {
        return chatRepository.getPagedMessagesByRoom(id, cursor ?: Instant.now(), limit)
    }

    fun save(messageDTO: ChatMessageDTO, userId: String): ChatMessage {
        val message = ChatMessage(id = UUID.randomUUID().toString(), message = messageDTO.message, room = messageDTO.room, sender = userId, timestamp = Instant.now())
        chatRepository.save(message)
        return message
    }
}