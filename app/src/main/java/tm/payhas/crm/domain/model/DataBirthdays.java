package tm.payhas.crm.domain.model;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public class DataBirthdays {
    private String date;
    private EntityUserInfo user;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EntityUserInfo getUser() {
        return user;
    }

    public void setUser(EntityUserInfo user) {
        this.user = user;
    }
}
