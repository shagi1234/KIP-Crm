package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataFile;

public class ResponseSingleFile extends Response {
    private DataFile data;

    public DataFile getData() {
        return data;
    }

    public void setData(DataFile data) {
        this.data = data;
    }
}
