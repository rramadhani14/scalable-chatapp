package dev.ramadhani

import dev.ramadhani.room.RoomDTO
import dev.ramadhani.room.RoomService
import dev.ramadhani.user.UserDTO
import dev.ramadhani.user.UserService
import io.quarkus.arc.Unremovable
import io.quarkus.logging.Log
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes


@ApplicationScoped
@Unremovable
class HouseKeeping(val roomService: RoomService, val userService: UserService) {
    fun init(@Observes ev: StartupEvent) {
        Log.info("HouseKeeping: init")
//        val room0 = roomService.save(RoomDTO("test-room"))
//        Log.info("test-room: ${room0.id}")
    }

}