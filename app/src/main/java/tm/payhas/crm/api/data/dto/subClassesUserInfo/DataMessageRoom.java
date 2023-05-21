package tm.payhas.crm.api.data.dto.subClassesUserInfo;

import com.google.gson.annotations.SerializedName;

public class DataMessageRoom {
    private String text;
    private int roomId;
    private String createdAt;
    private Room room;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public class Room {
        @SerializedName("_count")
        private Count count;

        public Count getCount() {
            return count;
        }

        public void setCount(Count count) {
            this.count = count;
        }

        public class Count {
            private int messages;

            public int getMessages() {
                return messages;
            }

            public void setMessages(int messages) {
                this.messages = messages;
            }
        }
    }
}

