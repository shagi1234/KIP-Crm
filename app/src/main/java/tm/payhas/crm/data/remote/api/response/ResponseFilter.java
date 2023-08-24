package tm.payhas.crm.data.remote.api.response;

import java.util.ArrayList;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataFilter;

public class ResponseFilter extends Response {
    private ArrayList<DataFilter> data;

    public ArrayList<DataFilter> getData() {
        return data;
    }

    public void setData(ArrayList<DataFilter> data) {
        this.data = data;
    }
}
