package tm.payhas.crm.data.remote.api.request;

import java.util.ArrayList;

import tm.payhas.crm.domain.model.DataAttachment;

public class RequestTaskComment {
    private int id;
    private String text;
    private int taskId;
    private ArrayList<DataAttachment> files;

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

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public ArrayList<DataAttachment> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<DataAttachment> files) {
        this.files = files;
    }
}
