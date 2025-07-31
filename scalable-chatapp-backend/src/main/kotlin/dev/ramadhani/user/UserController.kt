package dev.ramadhani.user

import dev.ramadhani.auth.UserPrincipalDTO
import io.quarkus.resteasy.reactive.links.RestLink
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("api/v1/user")
class UserController(private val userService: UserService) {

    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Path("/me")
    @GET()
    @RestLink(rel = "me")
//    @InjectRestLinks(RestLinkType.INSTANCE)
    @Produces(MediaType.APPLICATION_JSON)
    fun getCurrentUser(): Uni<User?> {
        val username = identity.identity.getPrincipal(UserPrincipalDTO::class.java).username
        return userService.getCurrentUser(username)
    }

    @Path("/{id}")
    @GET()
    @RestLink(rel = "self")
//    @InjectRestLinks(RestLinkType.INSTANCE)
    @Produces(MediaType.APPLICATION_JSON)
    fun getUser(@PathParam("id") userId: String): Uni<User?> {
        return userService.getUser(identity.identity.principal.name)
    }

}