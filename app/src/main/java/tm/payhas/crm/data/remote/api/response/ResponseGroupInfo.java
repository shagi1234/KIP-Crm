package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataGroupInfo;

public class ResponseGroupInfo extends Response {
    private DataGroupInfo data;

    public DataGroupInfo getData() {
        return data;
    }

    public void setData(DataGroupInfo data) {
        this.data = data;
    }
}
