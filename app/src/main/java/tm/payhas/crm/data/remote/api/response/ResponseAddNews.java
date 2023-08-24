package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataNews;

public class ResponseAddNews extends Response {
    private DataNews data;

    public DataNews getData() {
        return data;
    }

    public void setData(DataNews data) {
        this.data = data;
    }
}
