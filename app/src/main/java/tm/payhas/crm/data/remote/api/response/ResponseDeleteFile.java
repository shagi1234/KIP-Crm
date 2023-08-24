package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataDeleteFile;

public class ResponseDeleteFile extends Response {
    private DataDeleteFile data;

    public DataDeleteFile getData() {
        return data;
    }

    public void setData(DataDeleteFile data) {
        this.data = data;
    }
}
