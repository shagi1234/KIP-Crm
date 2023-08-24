package tm.payhas.crm.domain.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public interface HelperAddProject {

    void getExecutorUser(EntityUserInfo id);

    void getProjectUsers(ArrayList<EntityUserInfo> userSelectedList);
}
