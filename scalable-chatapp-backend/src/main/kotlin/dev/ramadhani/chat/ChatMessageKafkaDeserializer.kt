package dev.ramadhani.chat

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer

class ChatMessageKafkaDeserializer: ObjectMapperDeserializer<ChatMessage>(ChatMessage::class.java) {
}