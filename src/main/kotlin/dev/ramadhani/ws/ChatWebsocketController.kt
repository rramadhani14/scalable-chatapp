package dev.ramadhani.ws

import io.quarkus.logging.Log
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.websockets.next.*
import jakarta.inject.Inject

@Authenticated
@WebSocket(path = "/ws/v1/chat")
class ChatWebsocketController {

    @Inject
    lateinit var currentIdentity: SecurityIdentity

    @OnOpen
    fun onOpen(connection: WebSocketConnection) {
        connection.userData()?.put(UserData.TypedKey.forString("user"), currentIdentity.principal.name)
        Log.info("WebSocket connection opened")
    }

    @OnTextMessage
    fun onTextMessage(command: WsChatCommand, connection: WebSocketConnection) {
        command.execute(connection)
    }

    @OnClose
    fun onClose(connection: WebSocketConnection, reason: CloseReason) {
        Log.info("WS connection: ${connection.id()} is closed, reason: $reason")
        UnsubscribeAllRoomsCommand().execute(connection)
    }
}