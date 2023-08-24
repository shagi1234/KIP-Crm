package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataFcmToken;

public class ResponseFcmToken extends Response {
    private DataFcmToken data;

    public DataFcmToken getData() {
        return data;
    }

    public void setData(DataFcmToken data) {
        this.data = data;
    }
}
