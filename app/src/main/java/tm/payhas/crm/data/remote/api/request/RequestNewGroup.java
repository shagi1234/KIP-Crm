package tm.payhas.crm.data.remote.api.request;

import java.util.ArrayList;

public class RequestNewGroup {
    private String name;
    private ArrayList<Integer> participants;
    private String avatar;
    private String type;

    public RequestNewGroup(String name, ArrayList<Integer> participants, String avatar, String type) {
        this.name = name;
        this.participants = participants;
        this.avatar = avatar;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Integer> participants) {
        this.participants = participants;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}