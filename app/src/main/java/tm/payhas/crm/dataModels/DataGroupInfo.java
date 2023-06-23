package tm.payhas.crm.dataModels;

import java.util.ArrayList;

import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.api.data.dto.subClassesUserInfo.DataMessageRoom;

public class DataGroupInfo {
    private DataMessageRoom room;
    private ArrayList<DtoUserInfo> participants;

    public ArrayList<DtoUserInfo> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<DtoUserInfo> participants) {
        this.participants = participants;
    }

    public DataMessageRoom getRoom() {
        return room;
    }

    public void setRoom(DataMessageRoom room) {
        this.room = room;
    }
}
