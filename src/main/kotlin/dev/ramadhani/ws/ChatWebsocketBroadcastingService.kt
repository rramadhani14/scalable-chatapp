package dev.ramadhani.ws

import dev.ramadhani.chat.ChatMessage
import dev.ramadhani.chat.ChatMessageDTO
import dev.ramadhani.chat.ChatService
import io.quarkus.arc.Unremovable
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.Executors


@ApplicationScoped
@Unremovable
class ChatWebsocketBroadcastingService(private val chatService: ChatService, private val subscriptionService: ChatWebsocketSubscriptionService, private val kafkaProducer: ChatWebsocketBroadcastingKafkaProducer) {
    private val broadcastingExecutor = Executors.newCachedThreadPool()
    fun saveAndBroadcastToRoom(messageDTO: ChatMessageDTO, userId: String) {
        chatService.save(messageDTO, userId)
            .runSubscriptionOn(broadcastingExecutor)
            .subscribe()
            .with {
                kafkaProducer.broadcastToRoom(it)
            }
    }

    fun broadcastToRoom(message: ChatMessage) {
        Uni.createFrom().nullItem<ChatMessage>()
            .runSubscriptionOn(broadcastingExecutor)
            .subscribe()
            .with {
                subscriptionService.getSubscriptions()[message.room]?.forEach{ it.sendTextAndAwait(message) }
            }
    }
}