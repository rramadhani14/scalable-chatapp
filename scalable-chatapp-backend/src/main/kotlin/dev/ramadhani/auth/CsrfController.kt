package dev.ramadhani.auth

import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.ResponseStatus


@Path("api/v1/auth/csrf")
class CsrfController {


    @Inject
    lateinit var identity: CurrentIdentityAssociation

    @Path("")
    @GET
    fun getCsrfToken(): Uni<Response> {
        val csrf = identity.identity.principal.name.split(":")[2]
        return Uni.createFrom().item(Response.noContent().cookie(NewCookie.Builder("quarkus-csrf-token").sameSite(NewCookie.SameSite.STRICT).httpOnly(false).secure(false).value(csrf).build()).build())
    }
}