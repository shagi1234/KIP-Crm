package tm.payhas.crm.api.response;

import java.util.ArrayList;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataFolder;

public class ResponseDataFolder extends Response {
    private ArrayList<DataFolder> data;

    public ArrayList<DataFolder> getData() {
        return data;
    }

    public void setData(ArrayList<DataFolder> data) {
        this.data = data;
    }
}
