package tm.payhas.crm.dataModels;

import java.util.ArrayList;

public class DataProject {
    private int id;
    private String name;
    private String description;
    private String startsAt;
    private String deadline;
    private String status;
    private DataAttachment file;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private ArrayList<DataProjectUsers> projectParticipants;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataAttachment getFile() {
        return file;
    }

    public void setFile(DataAttachment file) {
        this.file = file;
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

    public ArrayList<DataProjectUsers> getProjectParticipants() {
        return projectParticipants;
    }

    public void setProjectParticipants(ArrayList<DataProjectUsers> projectParticipants) {
        this.projectParticipants = projectParticipants;
    }
}
