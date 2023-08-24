package tm.payhas.crm.data.localdb.entity;


import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import tm.payhas.crm.domain.model.DataRoom;

@Entity(tableName = "table_groups")
public class EntityGroup {
    @PrimaryKey
    private int id;
    private String name;
    private String avatar;
    private String createdAt;
    private String deletedAt;
    @Embedded
    private DataRoom messageRoom;

    public EntityGroup(int id, String name, String avatar, String createdAt, String deletedAt, DataRoom messageRoom) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.messageRoom = messageRoom;
    }

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

    public DataRoom getMessageRoom() {
        return messageRoom;
    }

    public void setMessageRoom(DataRoom messageRoom) {
        this.messageRoom = messageRoom;
    }
}
