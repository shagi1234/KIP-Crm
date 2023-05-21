package tm.payhas.crm.api.data.dto.subClassesUserInfo;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataMessage;
import tm.payhas.crm.dataModels.DataMessageTarget;

public class DataRoomChat {

    private String chatData;
    private ArrayList<DataMessageTarget> messages;

    public String getChatData() {
        return chatData;
    }

    public void setChatData(String chatData) {
        this.chatData = chatData;
    }

    public ArrayList<DataMessageTarget> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<DataMessageTarget> messages) {
        this.messages = messages;
    }
}
