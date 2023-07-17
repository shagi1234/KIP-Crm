package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataFolder;

public class ResponseNewFolder extends Response {
    private DataFolder data;

    public DataFolder getData() {
        return data;
    }

    public void setData(DataFolder data) {
        this.data = data;
    }
}
