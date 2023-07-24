package tm.payhas.crm.api.response;

import java.util.ArrayList;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataFilter;

public class ResponseFilter extends Response {
    private ArrayList<DataFilter> data;

    public ArrayList<DataFilter> getData() {
        return data;
    }

    public void setData(ArrayList<DataFilter> data) {
        this.data = data;
    }
}
