package tm.payhas.crm.api.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataUserGroup {
    @SerializedName("private")
    private ArrayList<DtoUserInfo> usersPrivate;

    public ArrayList<DtoUserInfo> getUsersPrivate() {
        return usersPrivate;
    }

    public void setUsersPrivate(ArrayList<DtoUserInfo> usersPrivate) {
        this.usersPrivate = usersPrivate;
    }
}
