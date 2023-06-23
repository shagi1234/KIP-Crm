package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataProject;

public class ResponseOneProject extends Response {
    private DataProject data;

    public DataProject getData() {
        return data;
    }

    public void setData(DataProject data) {
        this.data = data;
    }
}
