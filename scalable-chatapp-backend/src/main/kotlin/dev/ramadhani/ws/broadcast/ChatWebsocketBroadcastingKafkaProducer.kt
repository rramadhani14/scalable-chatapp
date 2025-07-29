package dev.ramadhani.ws.broadcast

import dev.ramadhani.chat.ChatMessage
import dev.ramadhani.util.RoomTopicPartitioner
import io.quarkus.arc.Unremovable
import io.smallrye.common.annotation.Identifier
import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.concurrent.ConcurrentHashMap


@ApplicationScoped
@Unremovable
class ChatWebsocketBroadcastingKafkaProducer(@Identifier("default-kafka-broker") private val config: Map<String, Object>, private val roomTopicPartitioner: RoomTopicPartitioner) {
    private val currentProducerTopic = ConcurrentHashMap<String, KafkaProducer<String, ChatMessage>>()

    fun broadcastToRoom(message: ChatMessage) {
        val topic = roomTopicPartitioner.getRoomTopic(message.room)
        val producer = currentProducerTopic.computeIfAbsent(topic) {
            return@computeIfAbsent KafkaProducer<String, ChatMessage>(config);
        }
        producer.send(ProducerRecord(topic, message.room, message))
    }
}