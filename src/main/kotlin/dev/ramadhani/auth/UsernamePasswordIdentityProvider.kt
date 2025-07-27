package dev.ramadhani.auth

import dev.ramadhani.user.UserService
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.security.credential.PasswordCredential
import io.quarkus.security.identity.AuthenticationRequestContext
import io.quarkus.security.identity.IdentityProvider
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest
import io.quarkus.security.runtime.QuarkusSecurityIdentity
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UsernamePasswordIdentityProvider(val userService: UserService): IdentityProvider<UsernamePasswordAuthenticationRequest> {

    override fun getRequestType() = UsernamePasswordAuthenticationRequest::class.java

    fun authenticate(username: String, password: String): Uni<SecurityIdentity> {
        return authenticate(UsernamePasswordAuthenticationRequest(username, PasswordCredential(password.toCharArray())), null)
    }

    override fun authenticate(request: UsernamePasswordAuthenticationRequest?, context: AuthenticationRequestContext?): Uni<SecurityIdentity> {
        val username = request?.username ?: ""
        val password = String(request?.password?.password ?: return Uni.createFrom().nullItem())
        return userService.getUserUni(username)
            .onItem()
            .transform {
                if(it != null && BcryptUtil.matches(password, it.password)) {
                        val identity = QuarkusSecurityIdentity.builder()
                            .setPrincipal(UserPrincipalDTO(it.id, it.username))
                            .addRoles(setOf("USER"))
                            .build()
                    return@transform identity
                } else {
                    null
                }
            }
    }
}