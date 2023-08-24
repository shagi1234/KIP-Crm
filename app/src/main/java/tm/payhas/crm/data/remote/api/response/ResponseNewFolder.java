package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataFolder;

public class ResponseNewFolder extends Response {
    private DataFolder data;

    public DataFolder getData() {
        return data;
    }

    public void setData(DataFolder data) {
        this.data = data;
    }
}
