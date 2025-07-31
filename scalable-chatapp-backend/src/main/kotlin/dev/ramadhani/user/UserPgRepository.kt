package dev.ramadhani.user

import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.logging.Log
import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.sqlclient.Pool
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import java.time.Duration
import java.util.UUID
import java.util.concurrent.Executors
import java.util.function.Consumer
import java.util.function.Supplier


@ApplicationScoped
class UserPgRepository (private val client: Pool) {
    val executor = Executors.newSingleThreadExecutor()
    fun initUsers(@Observes ev: StartupEvent) {
        Log.info("Starting user PgRepository $ev")
        client
            .preparedQuery("INSERT INTO users(id, username, password) VALUES ($1, $2, $3) ON CONFLICT (username) DO NOTHING")
            .executeBatch(listOf(
                Tuple.of(UUID.randomUUID().toString(), "test", BcryptUtil.bcryptHash("test123")),
                Tuple.of(UUID.randomUUID().toString(), "admin", BcryptUtil.bcryptHash("admin123")),
            ))
            .subscribe()
            .with(
                { result -> Log.info("Success: $result") },
                { err -> Log.error("Failed: $err") }
            )
    }

    fun getUsers(): Uni<List<User>> {
        return client
            .preparedQuery("SELECT id, username, password FROM users")
            .execute()
            .onItem()
            .transform { it.map { row -> User(row.getString("id"), row.getString("username"), row.getString("password")) } }
    }

    fun getUser(username: String): Uni<User?> {
        return client
            .preparedQuery("SELECT id, username, password FROM users WHERE username = $1 LIMIT 1")
            .execute(Tuple.of(username))
            .onItem()
            .transform { it.map { row -> User(row.getString("id"), row.getString("username"), row.getString("password")) }.firstOrNull()}
    }

    fun getUserUni(username: String): Uni<User?> {
        return client
            .preparedQuery("SELECT id, username, password FROM users WHERE username = $1 LIMIT 1")
            .execute(Tuple.of(username))
            .onItem()
            .transform { it.map { row -> User(row.getString("id"), row.getString("username"), row.getString("password")) }.getOrNull(0)}
    }

    fun updatePassword(user: User): Uni<Any> {
        return client
            .preparedQuery("UPDATE users SET password = $1 WHERE id = $2")
            .execute(Tuple.of(user.password, user.id))
            .chain(Supplier { Uni.createFrom().nullItem() })
    }

    fun updateUsername(user: User): Uni<Any> {
        return client
            .preparedQuery("UPDATE users SET username = $2 WHERE id = $1")
            .execute(Tuple.of(user.password, user.id))
            .chain(Supplier { Uni.createFrom().nullItem() })
    }

    fun saveNewUser(user: User): Uni<User> {
        return client
            .preparedQuery("INSERT INTO users(id, username, password) VALUES ($1, $2, $3) RETURNING id, username, password")
            .execute(Tuple.of(user.id, user.username, user.password))
            .onItem()
            .transform { it.map { row -> User(row.getString("id"), row.getString("username"), "*****") }.first() }
    }
}