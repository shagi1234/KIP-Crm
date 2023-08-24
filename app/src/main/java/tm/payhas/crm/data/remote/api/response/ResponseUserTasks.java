package tm.payhas.crm.data.remote.api.response;

import java.util.ArrayList;

import tm.payhas.crm.domain.model.DataTask;

public class ResponseUserTasks {
    private int pageCount;
    private int count;
    private ArrayList<DataTask> tasks;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<DataTask> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<DataTask> tasks) {
        this.tasks = tasks;
    }
}
