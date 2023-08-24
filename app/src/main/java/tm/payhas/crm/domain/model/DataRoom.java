package tm.payhas.crm.domain.model;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

public class DataRoom {
    @SerializedName("text")
    private String textRoom = "";
    @SerializedName("roomId")
    private int roomIdRoom = 0;
    @SerializedName("createdAt")
    private String createdAtRoom = "";
    @Embedded
    private DtoRoom room;

    public String getTextRoom() {
        return textRoom;
    }

    public void setTextRoom(String textRoom) {
        this.textRoom = textRoom;
    }

    public int getRoomIdRoom() {
        return roomIdRoom;
    }

    public void setRoomIdRoom(int roomIdRoom) {
        this.roomIdRoom = roomIdRoom;
    }

    public String getCreatedAtRoom() {
        return createdAtRoom;
    }

    public void setCreatedAtRoom(String createdAtRoom) {
        this.createdAtRoom = createdAtRoom;
    }

    public DtoRoom getRoom() {
        return room;
    }

    public void setRoom(DtoRoom room) {
        this.room = room;
    }


}

