package dev.ramadhani.config

import io.quarkus.arc.Unremovable
import io.smallrye.common.annotation.Identifier
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.KafkaAdminClient

@ApplicationScoped
@Unremovable
class KafkaConfig {
    @Produces
    fun kafkaAdminClient(@Identifier("default-kafka-broker") config: Map<String, Object>): AdminClient {
        return KafkaAdminClient.create(config.toMap())
    }
}