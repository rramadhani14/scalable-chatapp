package dev.ramadhani.auth

import io.quarkus.security.AuthenticationFailedException
import io.quarkus.security.identity.AuthenticationRequestContext
import io.quarkus.security.identity.IdentityProvider
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.identity.request.TrustedAuthenticationRequest
import io.quarkus.security.runtime.QuarkusSecurityIdentity
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CookieIdentityProvider: IdentityProvider<TrustedAuthenticationRequest> {
    override fun getRequestType(): Class<TrustedAuthenticationRequest> = TrustedAuthenticationRequest::class.java

    override fun authenticate(
        request: TrustedAuthenticationRequest?,
        context: AuthenticationRequestContext?
    ): Uni<SecurityIdentity> {
        val cookiePrincipal = request?.principal?.split(":")
        if(cookiePrincipal != null && cookiePrincipal.size == 2) {
            val principal = UserPrincipalDTO(cookiePrincipal[0], cookiePrincipal[1])
            val identity = QuarkusSecurityIdentity.builder()
                .setPrincipal(principal)
                .addRoles(setOf("USER"))
                .build()
            return Uni.createFrom().item(identity)
        }
        return Uni.createFrom().nullItem()
    }
}