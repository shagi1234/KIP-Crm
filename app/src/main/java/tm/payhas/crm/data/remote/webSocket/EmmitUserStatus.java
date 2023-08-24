package tm.payhas.crm.data.remote.webSocket;

import tm.payhas.crm.domain.model.DataUserStatus;

public class EmmitUserStatus {
    String event;
    String channel;
    DataUserStatus data;

    public String getChannel() {
        return channel;
    }

    public void setData(DataUserStatus data) {
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public String getChanel() {
        return channel;
    }


    public void setEvent(String event) {
        this.event = event;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


}
