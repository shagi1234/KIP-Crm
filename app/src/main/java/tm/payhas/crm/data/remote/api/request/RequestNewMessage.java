package tm.payhas.crm.data.remote.api.request;

import com.google.gson.annotations.SerializedName;

import tm.payhas.crm.data.localdb.entity.EntityMessage;

public class RequestNewMessage {
    private String event;
    private EntityMessage data;
    private Integer id = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public EntityMessage getData() {
        return data;
    }

    public void setData(EntityMessage data) {
        this.data = data;
    }
}
