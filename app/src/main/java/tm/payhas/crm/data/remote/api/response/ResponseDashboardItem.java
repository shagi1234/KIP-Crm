package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataOneNews;

public class ResponseDashboardItem extends Response {
    private DataOneNews data;

    public DataOneNews getData() {
        return data;
    }

    public void setData(DataOneNews data) {
        this.data = data;
    }
}
