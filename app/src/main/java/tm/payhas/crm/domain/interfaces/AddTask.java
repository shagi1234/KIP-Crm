package tm.payhas.crm.domain.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.domain.model.DataProject;

public interface AddTask {
    void selectedProjectId(DataProject oneProject);

    void setExecutor(EntityUserInfo user);

    void getResponsibleUsers(ArrayList<EntityUserInfo> users);

    void getObserverUsers(ArrayList<EntityUserInfo> users);

}
