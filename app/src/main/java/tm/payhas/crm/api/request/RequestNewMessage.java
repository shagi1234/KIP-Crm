package tm.payhas.crm.api.request;

import tm.payhas.crm.dataModels.DataMessageTarget;

public class RequestNewMessage {
    private String event;
    private DataMessageTarget data;
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

    public DataMessageTarget getData() {
        return data;
    }

    public void setData(DataMessageTarget data) {
        this.data = data;
    }
}
