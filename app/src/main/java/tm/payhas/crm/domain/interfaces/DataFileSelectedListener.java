package tm.payhas.crm.domain.interfaces;

import java.util.ArrayList;

import tm.payhas.crm.domain.model.DataFolder;

public interface DataFileSelectedListener {

    void multiSelectedArray(ArrayList<DataFolder> selected);

    void setUnSelectable();
}
