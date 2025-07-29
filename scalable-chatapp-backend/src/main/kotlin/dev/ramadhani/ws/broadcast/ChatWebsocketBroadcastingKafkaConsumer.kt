package dev.ramadhani.ws.broadcast

import dev.ramadhani.chat.ChatMessage
import dev.ramadhani.chat.ChatMessageKafkaDeserializer
import dev.ramadhani.util.RoomTopicPartitioner
import io.quarkus.arc.Unremovable
import io.quarkus.logging.Log
import io.smallrye.common.annotation.Identifier
import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.regex.Pattern

@ApplicationScoped
@Unremovable
class ChatWebsocketBroadcastingKafkaConsumer(@Identifier("default-kafka-broker") private val config: Map<String, Object>, private val chatWebsocketBroadcastingService: ChatWebsocketBroadcastingService, private val roomTopicPartitioner: RoomTopicPartitioner, private val pollingExecutor: ExecutorService = Executors.newFixedThreadPool(4)) {
    private val currentSubscribedTopic = ConcurrentHashMap<String, Pair<KafkaConsumer<String, ChatMessage>, Future<*>>>()

    fun subscribeRoomTopic(roomId: String) {
        val topic = roomTopicPartitioner.getRoomTopic(roomId)
        currentSubscribedTopic.computeIfAbsent(topic) {
            val consumer = KafkaConsumer(config, StringDeserializer(), ChatMessageKafkaDeserializer())
            consumer.subscribe(Pattern.compile(topic))
            val future = pollingExecutor.submit {
                while (true) {
                    consumer.poll(Duration.ofMillis(2500)).forEach {
                        try{
                            chatWebsocketBroadcastingService.broadcastToRoom(it.value())
                        } catch (e: Exception) {
                            Log.error(e)
                        }
                    }
                }
            }
            return@computeIfAbsent Pair(consumer, future)
        }
    }
}