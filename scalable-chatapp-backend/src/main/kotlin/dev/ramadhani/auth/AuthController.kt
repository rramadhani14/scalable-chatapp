package dev.ramadhani.auth

import io.quarkus.security.identity.CurrentIdentityAssociation
import io.quarkus.vertx.http.runtime.security.FormAuthenticationMechanism
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext


@Path("api/v1/auth")
class AuthController(

) {
    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Path("/logout")
    @POST
    fun logout() {
        FormAuthenticationMechanism.logout(identity.identity)
    }

}