package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataTaskComment;

public class ResponseTaskComment extends Response {
    private DataTaskComment data;

    public DataTaskComment getData() {
        return data;
    }

    public void setData(DataTaskComment data) {
        this.data = data;
    }
}
