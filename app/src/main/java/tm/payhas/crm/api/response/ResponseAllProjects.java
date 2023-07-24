package tm.payhas.crm.api.response;

import java.util.ArrayList;

import tm.payhas.crm.dataModels.DataProject;

public class ResponseAllProjects {
    private ArrayList<DataProject> data;

    public ArrayList<DataProject> getData() {
        return data;
    }

    public void setData(ArrayList<DataProject> data) {
        this.data = data;
    }
}
