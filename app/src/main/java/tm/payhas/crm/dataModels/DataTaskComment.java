package tm.payhas.crm.dataModels;

import java.util.ArrayList;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class DataTaskComment {
    private int id;
    private String text;
    private ArrayList<DataAttachment> files;
    private String createdAt;
    private String deletedAt;
    private int userId;
    private int taskId;
    private DtoUserInfo user;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<DataAttachment> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<DataAttachment> files) {
        this.files = files;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public DtoUserInfo getUser() {
        return user;
    }

    public void setUser(DtoUserInfo user) {
        this.user = user;
    }
}
