package tm.payhas.crm.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import tm.payhas.crm.data.localdb.entity.EntityGroup;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public class DataUserGroup {
    @SerializedName("private")
    private ArrayList<EntityUserInfo> usersPrivate;
    private ArrayList<EntityGroup> groups;

    public ArrayList<EntityGroup> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<EntityGroup> groups) {
        this.groups = groups;
    }

    public ArrayList<EntityUserInfo> getUsersPrivate() {
        return usersPrivate;
    }

    public void setUsersPrivate(ArrayList<EntityUserInfo> usersPrivate) {
        this.usersPrivate = usersPrivate;
    }
}
