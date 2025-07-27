package dev.ramadhani.ws

import dev.ramadhani.chat.ChatMessage
import io.quarkus.arc.Unremovable
import io.smallrye.common.annotation.Identifier
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.admin.KafkaAdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.concurrent.ConcurrentHashMap


@ApplicationScoped
@Unremovable
class ChatWebsocketBroadcastingKafkaProducer(@Identifier("default-kafka-broker") private val config: Map<String, Object>) {
    private val currentProducerTopic = ConcurrentHashMap<String, KafkaProducer<String, ChatMessage>>()
    private val kafkaAdminClient = KafkaAdminClient.create(config.toMap())

    init {
        Uni.createFrom()
            .item {
                kafkaAdminClient.createTopics((0..4).map { NewTopic("chat.message.p-$it", 1, 1) }.toMutableList())
            }
            .subscribe()
    }

    fun broadcastToRoom(message: ChatMessage) {
        val partition = message.room.toCharArray().fold(0) { acc, c -> acc + c.code} % 5
        val topic = "chat.message.p-$partition"
        val producer = currentProducerTopic.computeIfAbsent(topic) {
            return@computeIfAbsent KafkaProducer<String, ChatMessage>(config);
        }
        producer.send(ProducerRecord(topic, message.room, message))
    }
}