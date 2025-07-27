package dev.ramadhani.ws

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import dev.ramadhani.chat.ChatMessageDTO
import dev.ramadhani.room.RoomService
import io.quarkus.runtime.annotations.RegisterForReflection
import io.quarkus.websockets.next.UserData
import io.quarkus.websockets.next.WebSocketConnection
import jakarta.enterprise.inject.spi.CDI


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(value = [
    JsonSubTypes.Type(value = InitChatCommand::class),
    JsonSubTypes.Type(value = SendMessageCommand::class),
    JsonSubTypes.Type(value = DeleteMessageCommand::class),
    JsonSubTypes.Type(value = SubscribeRoomCommand::class),
    JsonSubTypes.Type(value = UnsubscribeRoomCommand::class),
    JsonSubTypes.Type(value = UnsubscribeAllRoomsCommand::class)
])
abstract class WsChatCommand(open val body: Any) {
    abstract fun execute(connection: WebSocketConnection)
}
@RegisterForReflection
@JsonTypeName("SEND_MESSAGE")
class SendMessageCommand(override val body: ChatMessageDTO): WsChatCommand(body) {
    override fun execute(connection: WebSocketConnection) {
        val chatWebsocketBroadcastingService = CDI.current().select(ChatWebsocketBroadcastingService::class.java).get()
        val userId = connection.userData().get(UserData.TypedKey.forString("user"))
        chatWebsocketBroadcastingService.saveAndBroadcastToRoom(body, userId)
    }
}
@RegisterForReflection
@JsonTypeName("DELETE_MESSAGE")
class DeleteMessageCommand(override val body: ChatMessageDTO): WsChatCommand(body) {
    override fun execute(connection: WebSocketConnection) {
        val chatWebsocketBroadcastingService = CDI.current().select(ChatWebsocketBroadcastingService::class.java).get()
        val userId = connection.userData().get(UserData.TypedKey.forString("user"))
        chatWebsocketBroadcastingService.saveAndBroadcastToRoom(body, userId)
    }
}

@RegisterForReflection
@JsonTypeName("INIT_CHAT")
class InitChatCommand(override val body: Any): WsChatCommand(body) {
    override fun execute(connection: WebSocketConnection) {
        val roomService = CDI.current().select(RoomService::class.java).get()
        val rooms = roomService.getUserRooms(connection.userData().get(UserData.TypedKey.forString("user"))).map { it.id }
        SubscribeRoomCommand(rooms).execute(connection)
    }
}

@RegisterForReflection
@JsonTypeName("SUBSCRIBE_ROOMS")
class SubscribeRoomCommand(override val body: List<String>): WsChatCommand(body) {

    override fun execute(connection: WebSocketConnection) {
        val chatWebsocketSubscriptionService = CDI.current().select(ChatWebsocketSubscriptionService::class.java).get()
        chatWebsocketSubscriptionService.subscribeRooms(body, connection)

        val chatWebsocketBroadcastingKafkaConsumer = CDI.current().select(ChatWebsocketBroadcastingKafkaConsumer::class.java).get()
        body.forEach { id ->  chatWebsocketBroadcastingKafkaConsumer.subscribeRoomTopic(id)}

    }
}
@RegisterForReflection
@JsonTypeName("UNSUBSCRIBE_ROOM")
class UnsubscribeRoomCommand(override val body: String): WsChatCommand(body) {

    override fun execute(connection: WebSocketConnection) {
        val chatWebsocketSubscriptionService = CDI.current().select(ChatWebsocketSubscriptionService::class.java).get()
        chatWebsocketSubscriptionService.unsubscribeRoom(body, connection)
    }
}
@RegisterForReflection
@JsonTypeName("UNSUBSCRIBE_ALL_ROOMS")
class UnsubscribeAllRoomsCommand: WsChatCommand(mapOf<Any, Any>()) {

    override fun execute(connection: WebSocketConnection) {
        val chatWebsocketSubscriptionService = CDI.current().select(ChatWebsocketSubscriptionService::class.java).get()
        chatWebsocketSubscriptionService.unsubscribeAllRooms(connection)
    }
}