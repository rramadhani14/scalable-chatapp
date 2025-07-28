package dev.ramadhani.room

import dev.ramadhani.chat.ChatMessage
import dev.ramadhani.chat.ChatService
import dev.ramadhani.util.Page
import io.quarkus.resteasy.reactive.links.InjectRestLinks
import io.quarkus.resteasy.reactive.links.RestLink
import io.quarkus.resteasy.reactive.links.RestLinkType
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.UriInfo
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.jboss.resteasy.reactive.common.util.RestMediaType
import java.security.Principal
import java.time.Duration
import java.time.Instant


@Path("api/v1/rooms")
class RoomController(
    private val roomService: RoomService,
    private val chatService: ChatService
) {


    @GET
    @Produces(MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON)
    @InjectRestLinks
//    @RestLink(rel = "list")
    suspend fun getRooms(): Uni<List<Room>> {
        return roomService.getRooms()
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON)
    @RestLink(rel = "self")
    @InjectRestLinks(RestLinkType.INSTANCE)
    fun getRoom(@PathParam("id") id: String): Uni<Room?> {
        return roomService.getRoom(id)
    }

    //TODO: Need to add HATEOAS version somehow
    @GET
    @Path("/{id}/messages")
    @Produces(MediaType.APPLICATION_JSON)
    fun getChatMessages(@PathParam("id") roomId: String, @QueryParam("cursor") cursor: Instant?, @QueryParam("limit") limit: Int = 5): Uni<Page<ChatMessage, Instant>> {
        require(limit in 1..25) { "Limit must be set and between 1 and 25!" }
        return chatService.getPagedMessagesByRoom(roomId, cursor, limit)
    }

    @POST
    @Path("/{id}/join")
    @Produces(MediaType.APPLICATION_JSON)
    fun joinRoom(@PathParam("id") roomId: String, principal: Principal): Uni<Any> {
        return roomService.joinRoom(principal.name, roomId)
    }

    @POST
    @InjectRestLinks
    @RestLink(rel = "create")
    @Produces(MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON)
    fun createRoom(@RequestBody roomDTO: RoomDTO, uriInfo: UriInfo): Uni<Room> {
        return roomService.save(roomDTO)
    }

    @DELETE
    @Path("/{id}")
    @InjectRestLinks(RestLinkType.INSTANCE)
    @RestLink(rel = "delete", entityType = Room::class)
    @Produces(MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON)
    fun deleteRoom(@PathParam("id") id: String): Uni<Any> {
        return roomService.delete(id)
    }
}