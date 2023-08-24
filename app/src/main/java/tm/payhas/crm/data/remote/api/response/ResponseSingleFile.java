package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataFile;

public class ResponseSingleFile extends Response {
    private DataFile data;

    public DataFile getData() {
        return data;
    }

    public void setData(DataFile data) {
        this.data = data;
    }
}
