package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataTaskComment;

public class ResponseTaskComment extends Response {
    private DataTaskComment data;

    public DataTaskComment getData() {
        return data;
    }

    public void setData(DataTaskComment data) {
        this.data = data;
    }
}
