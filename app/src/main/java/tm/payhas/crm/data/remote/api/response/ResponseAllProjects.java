package tm.payhas.crm.data.remote.api.response;

import java.util.ArrayList;

import tm.payhas.crm.domain.model.DataProject;

public class ResponseAllProjects {
    private ArrayList<DataProject> data;

    public ArrayList<DataProject> getData() {
        return data;
    }

    public void setData(ArrayList<DataProject> data) {
        this.data = data;
    }
}
