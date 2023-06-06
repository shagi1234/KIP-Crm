package tm.payhas.crm.dataModels;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class DataMessageTarget {
    private Integer roomId = null;
    private Integer id = null;
    private String localId;
    private String type = "";
    private String text;
    private Integer forwardId = null;
    private Integer answerId = null;
    private int friendId;
    private String status;
    private int authorId;
    private DataAttachment attachment;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private DtoUserInfo author;
    private DataMessageTarget answering;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setForwardId(Integer forwardId) {
        this.forwardId = forwardId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public DataMessageTarget getAnswering() {
        return answering;
    }

    public void setAnswering(DataMessageTarget answering) {
        this.answering = answering;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getId() {
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

    public Integer getAnswerId() {
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
