package dev.ramadhani.chat

import dev.ramadhani.util.Identifiable
import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.Instant

@RegisterForReflection
data class ChatMessageDTO(val room: String, val message: String)
@RegisterForReflection
data class ChatMessage(override val id: String, val sender: String, val room: String, val message: String, val timestamp: Instant):
    Identifiable