-- data class Room(override val id: String, val name: String): Identifiable
CREATE TABLE room(id VARCHAR(36) PRIMARY KEY, room_name VARCHAR(128) NOT NULL);
INSERT INTO room(id, room_name) VALUES ('dcdfdee8-c347-41fd-b8be-c9182bd6ebb2', 'test-room');