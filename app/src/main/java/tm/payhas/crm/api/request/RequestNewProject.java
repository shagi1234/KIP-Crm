package tm.payhas.crm.api.request;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataAttachment;

public class RequestNewProject {
    private Integer id;
    private String name;
    private String description;
    private String deadline;
    private String startsAt;
    private int executorId;
    private ArrayList<Integer> projectParticipants;
    private DataAttachment file;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public int getExecutorId() {
        return executorId;
    }

    public void setExecutorId(int executorId) {
        this.executorId = executorId;
    }

    public ArrayList<Integer> getProjectParticipants() {
        return projectParticipants;
    }

    public void setProjectParticipants(ArrayList<Integer> projectParticipants) {
        this.projectParticipants = projectParticipants;
    }

    public DataAttachment getFile() {
        return file;
    }

    public void setFile(DataAttachment file) {
        this.file = file;
    }
}
