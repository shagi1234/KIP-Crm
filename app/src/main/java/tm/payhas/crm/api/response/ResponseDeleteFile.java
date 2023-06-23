package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataDeleteFile;

public class ResponseDeleteFile extends Response {
    private DataDeleteFile data;

    public DataDeleteFile getData() {
        return data;
    }

    public void setData(DataDeleteFile data) {
        this.data = data;
    }
}
