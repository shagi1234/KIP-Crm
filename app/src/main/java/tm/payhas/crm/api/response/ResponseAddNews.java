package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataNews;

public class ResponseAddNews extends Response {
    private DataNews data;

    public DataNews getData() {
        return data;
    }

    public void setData(DataNews data) {
        this.data = data;
    }
}
