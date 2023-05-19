package tm.payhas.crm.dataModels;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class DataMessageTarget {
    private int roomId;
    private int id;
    private String localId;
    private String type;
    private String text;
    private int forwardId;
    private int answerId;
    private int friendId;
    private String status;
    private int authorId;
    private DataAttachment attachment;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private DtoUserInfo author;

    public DataMessageTarget(int roomId, int id, String localId, String type, String text, int forwardId, int answerId, int friendId, String status, DataAttachment attachment) {
        this.roomId = roomId;
        this.id = id;
        this.localId = localId;
        this.type = type;
        this.text = text;
        this.forwardId = forwardId;
        this.answerId = answerId;
        this.friendId = friendId;
        this.status = status;
        this.attachment = attachment;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getId() {
        return id;
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
        return forwardId;
    }

    public void setForwardId(int forwardId) {
        this.forwardId = forwardId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getFriendId() {
        return friendId;
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
        return authorId;
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

    public DtoUserInfo getAuthor() {
        return author;
    }

    public void setAuthor(DtoUserInfo author) {
        this.author = author;
    }

    public DataAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(DataAttachment attachment) {
        this.attachment = attachment;
    }
}
