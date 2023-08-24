package tm.payhas.crm.domain.model;

import java.util.ArrayList;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public class DataTaskMembers {

    private ArrayList<EntityUserInfo> observers;
    private ArrayList<EntityUserInfo> responsible;
    private EntityUserInfo executor;
    private EntityUserInfo author;

    public ArrayList<EntityUserInfo> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<EntityUserInfo> observers) {
        this.observers = observers;
    }

    public ArrayList<EntityUserInfo> getResponsible() {
        return responsible;
    }

    public void setResponsible(ArrayList<EntityUserInfo> responsible) {
        this.responsible = responsible;
    }

    public EntityUserInfo getExecutor() {
        return executor;
    }

    public void setExecutor(EntityUserInfo executor) {
        this.executor = executor;
    }

    public EntityUserInfo getAuthor() {
        return author;
    }

    public void setAuthor(EntityUserInfo author) {
        this.author = author;
    }
}
