package dev.ramadhani.ws

import dev.ramadhani.chat.ChatMessage
import dev.ramadhani.chat.ChatMessageDTO
import dev.ramadhani.chat.ChatService
import io.quarkus.arc.Unremovable
import jakarta.enterprise.context.ApplicationScoped


@ApplicationScoped
@Unremovable
class ChatWebsocketBroadcastingService(private val chatService: ChatService, private val subscriptionService: ChatWebsocketSubscriptionService, private val kafkaProducer: ChatWebsocketBroadcastingKafkaProducer) {
    fun saveAndBroadcastToRoom(messageDTO: ChatMessageDTO, userId: String) {
        val message = chatService.save(messageDTO, userId)
        kafkaProducer.broadcastToRoom(message)
    }

    fun broadcastToRoom(message: ChatMessage) {
        subscriptionService.getSubscriptions()[message.room]?.forEach{ it.sendTextAndAwait(message) }
    }
}