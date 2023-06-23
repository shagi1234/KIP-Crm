package tm.payhas.crm.api.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataGroup;

public class DataUserGroup {
    @SerializedName("private")
    private ArrayList<DtoUserInfo> usersPrivate;
    private ArrayList<DataGroup> groups;

    public ArrayList<DataGroup> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<DataGroup> groups) {
        this.groups = groups;
    }

    public ArrayList<DtoUserInfo> getUsersPrivate() {
        return usersPrivate;
    }

    public void setUsersPrivate(ArrayList<DtoUserInfo> usersPrivate) {
        this.usersPrivate = usersPrivate;
    }
}
