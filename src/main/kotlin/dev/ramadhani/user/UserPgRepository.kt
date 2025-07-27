package dev.ramadhani.user

import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.sqlclient.Pool
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import java.time.Duration
import java.util.UUID


@ApplicationScoped
class UserPgRepository(private val client: Pool) {

    fun initUsers(@Observes ev: StartupEvent) {
        client
            .preparedQuery("INSERT INTO users(id, username, password) VALUES ($1, $2, $3) ON CONFLICT (username) DO NOTHING")
            .executeBatch(listOf(
                Tuple.of(UUID.randomUUID().toString(), "test", BcryptUtil.bcryptHash("test123")),
                Tuple.of(UUID.randomUUID().toString(), "admin", BcryptUtil.bcryptHash("admin123")),
            ))
            .subscribe()
    }

    fun getUsers(): List<User> {
        return client
            .preparedQuery("SELECT id, username, password FROM users")
            .execute()
            .onItem()
            .transform { it.map { row -> User(row.getString("id"), row.getString("username"), row.getString("password")) } }
            .await()
            .atMost(Duration.ofSeconds(5))
    }

    fun getUser(username: String): User? {
        return client
            .preparedQuery("SELECT id, username, password FROM users WHERE username = $1 LIMIT 1")
            .execute(Tuple.of(username))
            .onItem()
            .transform { it.map { row -> User(row.getString("id"), row.getString("username"), row.getString("password")) }}
            .await()
            .atMost(Duration.ofSeconds(5))
            .getOrNull(0)
    }

    fun getUserUni(username: String): Uni<User?> {
        return client
            .preparedQuery("SELECT id, username, password FROM users WHERE username = $1 LIMIT 1")
            .execute(Tuple.of(username))
            .onItem()
            .transform { it.map { row -> User(row.getString("id"), row.getString("username"), row.getString("password")) }.getOrNull(0)}
    }

    fun updatePassword(user: User) {
        client
            .preparedQuery("UPDATE users SET password = $1 WHERE id = $2")
            .execute(Tuple.of(user.password, user.id))
            .await()
            .atMost(Duration.ofSeconds(5))
    }

    fun updateUsername(user: User) {
        client
            .preparedQuery("UPDATE users SET username = $2 WHERE id = $1")
            .execute(Tuple.of(user.password, user.id))
            .await()
            .atMost(Duration.ofSeconds(5))
    }

    fun saveNewUser(user: User) {
        client
            .preparedQuery("INSERT INTO users(id, username, password) VALUES ($1, $2, $3)")
            .execute(Tuple.of(user.id, user.username, user.password))
            .await()
            .atMost(Duration.ofSeconds(5))
    }
}