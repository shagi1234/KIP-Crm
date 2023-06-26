package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataTask;

public class ResponseOneTask extends Response {
    private DataTask data;

    public DataTask getData() {
        return data;
    }

    public void setData(DataTask data) {
        this.data = data;
    }
}
