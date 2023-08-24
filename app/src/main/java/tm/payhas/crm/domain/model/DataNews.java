package tm.payhas.crm.domain.model;

import java.util.ArrayList;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public class DataNews {
    private int id;
    private String title;
    private String content;
    private String type;
    private int authorId;
    private String createdAt;
    private String deletedAt;
    private EntityUserInfo author;
    private ArrayList<DataComments> comments;
    private ArrayList<String> attachments;

    public ArrayList<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<String> attachments) {
        this.attachments = attachments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public EntityUserInfo getAuthor() {
        return author;
    }

    public void setAuthor(EntityUserInfo author) {
        this.author = author;
    }

    public ArrayList<DataComments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<DataComments> comments) {
        this.comments = comments;
    }
}
