package tm.payhas.crm.dataModels;

import tm.payhas.crm.api.data.dto.subClassesUserInfo.DataMessageRoom;

public class DataGroup {
    private int id;
    private String name;
    private String avatar;
    private String createdAt;
    private String deletedAt;
    private DataMessageRoom messageRoom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public DataMessageRoom getMessageRoom() {
        return messageRoom;
    }

    public void setMessageRoom(DataMessageRoom messageRoom) {
        this.messageRoom = messageRoom;
    }
}
