package tm.payhas.crm.api.data.response;

import java.util.ArrayList;

import tm.payhas.crm.api.data.dto.subClassesUserInfo.DataRoomChat;

public class ResponseChatRoom {
    private ArrayList<DataRoomChat> data;

    public ArrayList<DataRoomChat> getData() {
        return data;
    }

    public void setData(ArrayList<DataRoomChat> data) {
        this.data = data;
    }
}
