package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataMessageTarget;

public class ResponseOneMessage extends Response {
    private DataMessageTarget data;

    public DataMessageTarget getData() {
        return data;
    }

    public void setData(DataMessageTarget data) {
        this.data = data;
    }
}
