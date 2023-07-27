package tm.payhas.crm.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class DataTask {
    private int id;
    private boolean isAuthor;
    private boolean isExecutor;
    private int timeOut;
    private String name;
    private String description;
    private String startsAt;
    private String finishesAt;
    private String status;
    private String priority;
    private int projectId;
    private String reminderType;
    private int authorId;
    private String createdAt;
    private String deletedAt;
    private ArrayList<String> remindAt;
    private ArrayList<DataProject.UserInTask> responsibleUsers;
    private ArrayList<DataProject.UserInTask> observerUsers;
    private ArrayList<DataTaskComment> comments;
    private ArrayList<DataChecklist> checklists;
    private ArrayList<DataAttachment> files;
    private DataProject project;
    private int executorId;
    private DtoUserInfo executor;
    @SerializedName("_count")
    private Count count;

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public DtoUserInfo getExecutor() {
        return executor;
    }

    public void setExecutor(DtoUserInfo executor) {
        this.executor = executor;
    }

    public int getExecutorId() {
        return executorId;
    }

    public void setExecutorId(int executorId) {
        this.executorId = executorId;
    }

    public DataProject getProject() {
        return project;
    }

    public void setProject(DataProject project) {
        this.project = project;
    }

    public ArrayList<DataAttachment> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<DataAttachment> files) {
        this.files = files;
    }

    public ArrayList<String> getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(ArrayList<String> remindAt) {
        this.remindAt = remindAt;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public ArrayList<DataChecklist> getChecklists() {
        return checklists;
    }

    public void setChecklists(ArrayList<DataChecklist> checklists) {
        this.checklists = checklists;
    }

    public ArrayList<DataTaskComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<DataTaskComment> comments) {
        this.comments = comments;
    }

    public ArrayList<DataProject.UserInTask> getResponsibleUsers() {
        return responsibleUsers;
    }

    public void setResponsibleUsers(ArrayList<DataProject.UserInTask> responsibleUsers) {
        this.responsibleUsers = responsibleUsers;
    }

    public ArrayList<DataProject.UserInTask> getObserverUsers() {
        return observerUsers;
    }

    public void setObserverUsers(ArrayList<DataProject.UserInTask> observerUsers) {
        this.observerUsers = observerUsers;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFinishesAt() {
        return finishesAt;
    }

    public void setFinishesAt(String finishesAt) {
        this.finishesAt = finishesAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
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

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    public boolean isExecutor() {
        return isExecutor;
    }

    public void setExecutor(boolean executor) {
        isExecutor = executor;
    }

    public class RemindTime {
        private String remindAt;

        public String getRemindAt() {
            return remindAt;
        }

        public void setRemindAt(String remindAt) {
            this.remindAt = remindAt;
        }
    }

    public class Count {

        private int checklists;
        private int comments;
        private int observerUsers;
        private int responsibleUsers;

        public int getChecklists() {
            return checklists;
        }

        public void setChecklists(int checklists) {
            this.checklists = checklists;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getObserverUsers() {
            return observerUsers;
        }

        public void setObserverUsers(int observerUsers) {
            this.observerUsers = observerUsers;
        }

        public int getResponsibleUsers() {
            return responsibleUsers;
        }

        public void setResponsibleUsers(int responsibleUsers) {
            this.responsibleUsers = responsibleUsers;
        }
    }

}
