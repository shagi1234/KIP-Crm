package tm.payhas.crm.api.response;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataMessageTarget;

public class ResponseRoomMessages {
    private ArrayList<DataMessageTarget> data;

    public ArrayList<DataMessageTarget> getData() {
        return data;
    }

    public void setMessages(ArrayList<DataMessageTarget> data) {
        this.data = data;
    }
}
