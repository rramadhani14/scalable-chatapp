CREATE TABLE user_room(user_id CHAR(36), room_id CHAR(36), PRIMARY KEY (user_id, room_id), CONSTRAINT fk_user_room_user_id FOREIGN KEY (user_id) REFERENCES users(id), CONSTRAINT fk_user_room_room_id FOREIGN KEY (room_id) REFERENCES rooms(id));
CREATE INDEX idx_user_room_user_id ON user_room(user_id);
CREATE INDEX idx_user_room_room_id ON user_room(room_id);