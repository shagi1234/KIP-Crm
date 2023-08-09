package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataGroup;

public class ResponseOneGroup extends Response {
    private DataGroup data;

    public DataGroup getData() {
        return data;
    }

    public void setData(DataGroup data) {
        this.data = data;
    }
}
