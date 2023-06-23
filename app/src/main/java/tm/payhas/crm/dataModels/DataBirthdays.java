package tm.payhas.crm.dataModels;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class DataBirthdays {
    private String date;
    private DtoUserInfo user;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DtoUserInfo getUser() {
        return user;
    }

    public void setUser(DtoUserInfo user) {
        this.user = user;
    }
}
