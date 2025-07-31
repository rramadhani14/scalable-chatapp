package dev.ramadhani.util

import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.KafkaAdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class RoomTopicPartitioner(@ConfigProperty(name = "roomsPerTopicSize", defaultValue = "5") private val roomsPerTopicSize: Int, private val kafkaAdminClient: AdminClient) {
    init {
        Uni.createFrom()
            .item {
                kafkaAdminClient.createTopics((0..roomsPerTopicSize).map { NewTopic("chat.message.bucket-$it", 1, 1) }.toMutableList())
            }
            .subscribe()
            .with(
                { result -> Log.info("Topics successfully created: $result") },
                { ex -> Log.error("Topics failed to be created", ex) }
            )
    }

    fun getRoomTopic(name: String): String {
        val partition = name.toCharArray().fold(0) { acc, c -> acc + c.code} % roomsPerTopicSize
        val topic = "chat.message.bucket-$partition"
        return topic
    }
}