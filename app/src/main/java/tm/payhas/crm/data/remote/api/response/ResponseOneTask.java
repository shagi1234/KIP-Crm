package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataTask;

public class ResponseOneTask extends Response {
    private DataTask data;

    public DataTask getData() {
        return data;
    }

    public void setData(DataTask data) {
        this.data = data;
    }
}
