package dev.ramadhani.ws

import io.quarkus.logging.Log
import io.quarkus.websockets.next.OpenConnections
import io.quarkus.websockets.next.WebSocketConnection
import io.vertx.core.buffer.Buffer
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@ApplicationScoped
class ChatWebsocketSubscriptionService(private val connections: OpenConnections) {
    private val subscriptions  = ConcurrentHashMap<String, ConcurrentLinkedDeque<WebSocketConnection>>()
    private val scheduledExecutor = Executors.newSingleThreadScheduledExecutor()

    @PostConstruct
    fun init() {
        scheduledExecutor.scheduleAtFixedRate(
            fun() {
                Log.info("Connections: " + connections.count())
                connections.forEach { conn ->
                    conn.sendPing(Buffer.buffer("ping"))
                }
            },
            5,
            15,
            TimeUnit.SECONDS
        )
    }

    fun execute(command: WsChatCommand, connection: WebSocketConnection) {
        command.execute(connection)
    }

    fun subscribeRooms(ids: List<String>, connection: WebSocketConnection) {
        connections.findByConnectionId(connection.id()).ifPresent {
            ids.forEach { id ->
                subscriptions.computeIfAbsent(id) { ConcurrentLinkedDeque() }.add(connection)
            }
        }
    }

    fun unsubscribeRoom(id: String, connection: WebSocketConnection) {
        connections.findByConnectionId(connection.id()).ifPresent { conn ->
            subscriptions[id]?.remove(conn)
        }
    }

    fun unsubscribeAllRooms(connection: WebSocketConnection) {
        connections.findByConnectionId(connection.id()).ifPresent { conn ->
            subscriptions.forEach { sub ->
                sub.value.remove(connection)
            }
        }
    }

    fun getSubscriptions(): Map<String, List<WebSocketConnection>> {
        return subscriptions.map { it.key to it.value.toList() }.toMap()
    }
}