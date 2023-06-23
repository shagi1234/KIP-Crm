package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataComments;

public class ResponseComment extends Response {
    private DataComments data;

    public DataComments getData() {
        return data;
    }

    public void setData(DataComments data) {
        this.data = data;
    }
}
