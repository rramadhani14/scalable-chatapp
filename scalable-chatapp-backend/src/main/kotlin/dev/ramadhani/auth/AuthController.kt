package dev.ramadhani.auth

import io.quarkus.security.identity.CurrentIdentityAssociation
import io.quarkus.vertx.http.runtime.security.FormAuthenticationMechanism
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext


@Path("api/v1/auth")
class AuthController(
    private val csrfService: CsrfService
) {
    @Path("/logout")
    @POST
    fun logout(identity: CurrentIdentityAssociation) {
        FormAuthenticationMechanism.logout(identity.identity)
    }

    @GET
    @Path("/csrf")
    @Produces(MediaType.APPLICATION_JSON)
    fun csrf(securityContext: SecurityContext): CsrfToken {
        return CsrfToken(csrfService.getToken(securityContext.userPrincipal.name))
    }
}