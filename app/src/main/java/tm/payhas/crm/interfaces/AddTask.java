package tm.payhas.crm.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataProject;

public interface AddTask {
    void selectedProjectId(DataProject oneProject);

    void selectedObserverList(ArrayList<Integer> userList);
}
