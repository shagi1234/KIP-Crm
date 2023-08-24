package tm.payhas.crm.data.remote.api.response;

import java.util.ArrayList;

import tm.payhas.crm.data.localdb.entity.EntityMessage;

public class ResponseRoomMessages {
    private ArrayList<EntityMessage> data;

    public ArrayList<EntityMessage> getData() {
        return data;
    }

    public void setMessages(ArrayList<EntityMessage> data) {
        this.data = data;
    }
}
