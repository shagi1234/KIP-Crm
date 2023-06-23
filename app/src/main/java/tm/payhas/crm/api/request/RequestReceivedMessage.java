package tm.payhas.crm.api.request;

import tm.payhas.crm.dataModels.DataMessageReceived;

public class RequestReceivedMessage {
    private String event;
    private DataMessageReceived data;
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

    public DataMessageReceived getData() {
        return data;
    }

    public void setData(DataMessageReceived data) {
        this.data = data;
    }
}
