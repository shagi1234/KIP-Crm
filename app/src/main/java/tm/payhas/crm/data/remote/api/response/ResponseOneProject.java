package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataProject;

public class ResponseOneProject extends Response {
    private DataProject data;

    public DataProject getData() {
        return data;
    }

    public void setData(DataProject data) {
        this.data = data;
    }
}
