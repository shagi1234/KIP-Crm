package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.api.data.DataFcmToken;

public class ResponseFcmToken extends Response {
    private DataFcmToken data;

    public DataFcmToken getData() {
        return data;
    }

    public void setData(DataFcmToken data) {
        this.data = data;
    }
}
