package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataGroupInfo;

public class ResponseGroupInfo extends Response {
    private DataGroupInfo data;

    public DataGroupInfo getData() {
        return data;
    }

    public void setData(DataGroupInfo data) {
        this.data = data;
    }
}
