package tm.payhas.crm.dataModels;

import java.util.ArrayList;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class DataTaskMembers {

    private ArrayList<DtoUserInfo> observers;
    private ArrayList<DtoUserInfo> responsible;
    private DtoUserInfo executor;
    private DtoUserInfo author;

    public ArrayList<DtoUserInfo> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<DtoUserInfo> observers) {
        this.observers = observers;
    }

    public ArrayList<DtoUserInfo> getResponsible() {
        return responsible;
    }

    public void setResponsible(ArrayList<DtoUserInfo> responsible) {
        this.responsible = responsible;
    }

    public DtoUserInfo getExecutor() {
        return executor;
    }

    public void setExecutor(DtoUserInfo executor) {
        this.executor = executor;
    }

    public DtoUserInfo getAuthor() {
        return author;
    }

    public void setAuthor(DtoUserInfo author) {
        this.author = author;
    }
}
