package tm.payhas.crm.data.remote.api.request;

import java.util.ArrayList;

public class RequestNewChecklist {
    private int taskId;
    private String name;
    private String startsAt;
    private String finishesAt;
    private ArrayList<Integer> executors;
    private Integer id = null;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<Integer> getExecutors() {
        return executors;
    }

    public void setExecutors(ArrayList<Integer> executors) {
        this.executors = executors;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
