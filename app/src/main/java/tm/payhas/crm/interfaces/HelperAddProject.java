package tm.payhas.crm.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.api.data.dto.DtoUserInfo;

public interface HelperAddProject {

    void getExecutorUser(DtoUserInfo id);

    void getProjectUsers(ArrayList<DtoUserInfo> userSelectedList);
}
