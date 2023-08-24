package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataComments;

public class ResponseComment extends Response {
    private DataComments data;

    public DataComments getData() {
        return data;
    }

    public void setData(DataComments data) {
        this.data = data;
    }
}
