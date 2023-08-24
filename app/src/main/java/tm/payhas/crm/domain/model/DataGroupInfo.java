package tm.payhas.crm.domain.model;

import java.util.ArrayList;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public class DataGroupInfo {
    private DataRoom room;
    private ArrayList<EntityUserInfo> participants;

    public ArrayList<EntityUserInfo> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<EntityUserInfo> participants) {
        this.participants = participants;
    }

    public DataRoom getRoom() {
        return room;
    }

    public void setRoom(DataRoom room) {
        this.room = room;
    }
}
