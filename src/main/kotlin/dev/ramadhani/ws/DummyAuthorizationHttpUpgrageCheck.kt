//package dev.ramadhani.ws
//
//import io.quarkus.vertx.http.runtime.VertxHttpConfig
//import io.quarkus.vertx.http.runtime.security.PersistentLoginManager
//import io.quarkus.websockets.next.HttpUpgradeCheck
//import io.quarkus.websockets.next.HttpUpgradeCheck.CheckResult
//import io.smallrye.mutiny.Uni
//import io.vertx.core.Vertx
//import io.vertx.ext.web.impl.RouterImpl
//import io.vertx.ext.web.impl.RoutingContextImpl
//import jakarta.enterprise.context.ApplicationScoped
//
//import java.time.Duration
//
//@ApplicationScoped
//class DummyAuthorizationHttpUpgrageCheck(
////    httpConfig: VertxHttpConfig
//    ): HttpUpgradeCheck {
////    private val persistentLoginManager: PersistentLoginManager = PersistentLoginManager(
////        httpConfig.encryptionKey().orElse(null),
////        httpConfig.auth().form().cookieName(),
////        httpConfig.auth().form().timeout().toMillis(),
////        httpConfig.auth().form().newCookieInterval().toMillis(),
////        httpConfig.auth().form().httpOnlyCookie(),
////        httpConfig.auth().form().cookieSameSite().name,
////        httpConfig.auth().form().cookiePath().orElse(null),
////        httpConfig.auth().form().cookieMaxAge().map(Duration::toSeconds).orElse(-1L)
////    )
//
//    override fun perform(context: HttpUpgradeCheck.HttpUpgradeContext?): Uni<CheckResult> {
////        val routingContext = RoutingContextImpl("", RouterImpl(Vertx.currentContext().owner()), context?.httpRequest(), setOf())
//        val auth = context
//            ?.httpRequest()
//            ?.headers()
//            ?.toList()
//            ?.find { it.key.lowercase() == "authorization" }
//            ?.value
////        val res = persistentLoginManager.restore(routingContext)
//
//        if(auth?.contains("test") == true) {
//            return CheckResult.permitUpgrade()
//        }
//
//        return CheckResult.rejectUpgrade(401)
//    }
//
//}