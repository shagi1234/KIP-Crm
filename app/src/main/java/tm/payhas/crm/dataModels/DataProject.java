package tm.payhas.crm.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class DataProject {
    private int id;
    private int timeOut;
    private String name;
    private String description;
    private String startsAt;
    private String deadline;
    private DataAttachment file;
    private String createdAt;
    private int authorId;
    private int executorId;
    private String updatedAt;
    private String deletedAt;
    private ArrayList<UserInTask> projectParticipants;
    private ArrayList<DataTask> tasks;
    private DtoUserInfo author;
    private DtoUserInfo executor;
    @SerializedName("_count")
    private Count count;

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public ArrayList<DataTask> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<DataTask> tasks) {
        this.tasks = tasks;
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

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getExecutorId() {
        return executorId;
    }

    public void setExecutorId(int executorId) {
        this.executorId = executorId;
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

    public ArrayList<UserInTask> getProjectParticipants() {
        return projectParticipants;
    }

    public void setProjectParticipants(ArrayList<UserInTask> projectParticipants) {
        this.projectParticipants = projectParticipants;
    }

    //    public ArrayList<DataTask> getTasks() {
//        return tasks;
//    }
//
//    public void setTasks(ArrayList<DataTask> tasks) {
//        this.tasks = tasks;
//    }

    public DtoUserInfo getAuthor() {
        return author;
    }

    public void setAuthor(DtoUserInfo author) {
        this.author = author;
    }

    public DtoUserInfo getExecutor() {
        return executor;
    }

    public void setExecutor(DtoUserInfo executor) {
        this.executor = executor;
    }

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public class Count {
        private int projectParticipants;
        private int tasks;

        public int getProjectParticipants() {
            return projectParticipants;
        }

        public void setProjectParticipants(int projectParticipants) {
            this.projectParticipants = projectParticipants;
        }

        public int getTasks() {
            return tasks;
        }

        public void setProjectTasks(int tasks) {
            this.tasks = tasks;
        }
    }

    public class UserInTask {
        private int projectId;
        private int userId;
        private DtoUserInfo user;

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public DtoUserInfo getUser() {
            return user;
        }

        public void setUser(DtoUserInfo user) {
            this.user = user;
        }
    }
}
