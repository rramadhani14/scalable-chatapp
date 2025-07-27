package dev.ramadhani.ws

import dev.ramadhani.chat.ChatMessage
import dev.ramadhani.chat.ChatMessageKafkaDeserializer
import io.quarkus.arc.Unremovable
import io.quarkus.logging.Log
import io.smallrye.common.annotation.Identifier
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.admin.KafkaAdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.regex.Pattern

@ApplicationScoped
@Unremovable
class ChatWebsocketBroadcastingKafkaConsumer(@Identifier("default-kafka-broker") private val config: Map<String, Object>, private val chatWebsocketBroadcastingService: ChatWebsocketBroadcastingService) {
    private val currentSubscribedTopic = ConcurrentHashMap<String, Pair<KafkaConsumer<String, ChatMessage>, Future<*>>>()
    private val kafkaAdminClient = KafkaAdminClient.create(config.toMap())
    private val pollingExecutor = Executors.newFixedThreadPool(4)

    init {
        Uni.createFrom()
            .item {
                kafkaAdminClient.createTopics((0..4).map { NewTopic("chat.message.p-$it", 1, 1) }.toMutableList())
            }
            .subscribe()
    }

    fun subscribeRoomTopic(roomId: String) {
        val partition = roomId.toCharArray().fold(0) { acc, c -> acc + c.code} % 5
        val topic = "chat.message.p-$partition"
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