package tm.payhas.crm.domain.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public interface HelperChecklist {
    void selectedUsersList(ArrayList<EntityUserInfo> users);
}
