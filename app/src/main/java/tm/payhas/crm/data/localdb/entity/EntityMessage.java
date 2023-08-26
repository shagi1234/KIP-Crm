package tm.payhas.crm.data.localdb.entity;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverters;


import java.util.UUID;

import tm.payhas.crm.domain.model.DataAnswer;
import tm.payhas.crm.domain.model.DataAttachment;
import tm.payhas.crm.domain.model.DataAuthor;

@ProvidedTypeConverter
@Entity(tableName = "table_message")
public class EntityMessage {
    private Integer id;
    @PrimaryKey
    @NonNull
    private String localId = UUID.randomUUID().toString();
    private Integer roomId;
    private String type = "";
    private String text;
    private Integer forwardId;
    private Integer answerId;
    private int friendId;
    private String status;
    private int authorId;
    @Embedded
    private DataAttachment attachment;
    private String createdAt;
    private String updatedAt;
    private String deletedAt = null;
    @TypeConverters
    @Embedded
    private DataAuthor author;
    @TypeConverters
    @Embedded(prefix = "prefix_")
    private DataAnswer answering;

    public DataAuthor getAuthor() {
        return author;
    }

    public void setAuthor(DataAuthor author) {
        this.author = author;
    }

    public DataAnswer getAnswering() {
        return answering;
    }

    public void setAnswering(DataAnswer answering) {
        this.answering = answering;
    }

    public Integer getRoomId() {
        return getSafeInt(roomId);
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getId() {
        return getSafeInt(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getForwardId() {
        return getSafeInt(forwardId);
    }

    public void setForwardId(int forwardId) {
        this.forwardId = forwardId;
    }

    public Integer getAnswerId() {
        return getSafeInt(answerId);
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getFriendId() {
        return getSafeInt(friendId);
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAuthorId() {
        return getSafeInt(authorId);
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }


    public DataAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(DataAttachment attachment) {
        this.attachment = attachment;
    }

    private int getSafeInt(Integer value) {
        return value != null ? value : 0;
    }
}
