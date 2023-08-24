package tm.payhas.crm.domain.model;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public class DataProjectUsers {
    private EntityUserInfo user;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public EntityUserInfo getUser() {
        return user;
    }

    public void setUser(EntityUserInfo user) {
        this.user = user;
    }
}
