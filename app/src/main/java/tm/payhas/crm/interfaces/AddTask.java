package tm.payhas.crm.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.dataModels.DataProject;

public interface AddTask {
    void selectedProjectId(DataProject oneProject);

    void setExecutor(DtoUserInfo user);

    void getResponsibleUsers(ArrayList<DtoUserInfo> users);

    void getObserverUsers(ArrayList<DtoUserInfo> users);

}
