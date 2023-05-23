package tm.payhas.crm.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataProjectUsers;
import tm.payhas.crm.model.ModelFile;

public interface MultiSelector {
    void multiSelectedArray(ArrayList<ModelFile> selected);

    void selectedUserList(ArrayList<DataProjectUsers> selected);
}
