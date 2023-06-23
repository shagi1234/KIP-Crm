package tm.payhas.crm.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataFolder;

public interface DataFileSelectedListener {

    void multiSelectedArray(ArrayList<DataFolder> selected);

    void setUnSelectable();
}
