
-- id: String, val sender: String, val room: String, val message: String, val timestamp: Instant
CREATE TABLE chat_message(id VARCHAR(36) NOT NULL PRIMARY KEY, sender VARCHAR(64) NOT NULL, room VARCHAR(36) NOT NULL, message TEXT, timestamp TIMESTAMPTZ);