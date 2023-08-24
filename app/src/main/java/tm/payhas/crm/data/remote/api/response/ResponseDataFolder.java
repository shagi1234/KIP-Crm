package tm.payhas.crm.data.remote.api.response;

import java.util.ArrayList;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataFolder;

public class ResponseDataFolder extends Response {
    private ArrayList<DataFolder> data;

    public ArrayList<DataFolder> getData() {
        return data;
    }

    public void setData(ArrayList<DataFolder> data) {
        this.data = data;
    }
}
