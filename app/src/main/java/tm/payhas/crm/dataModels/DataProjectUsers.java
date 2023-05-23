package tm.payhas.crm.dataModels;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class DataProjectUsers {
    private DtoUserInfo user;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public DtoUserInfo getUser() {
        return user;
    }

    public void setUser(DtoUserInfo user) {
        this.user = user;
    }
}
