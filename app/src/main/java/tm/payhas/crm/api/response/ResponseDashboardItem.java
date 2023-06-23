package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataOneNews;

public class ResponseDashboardItem extends Response {
    private DataOneNews data;

    public DataOneNews getData() {
        return data;
    }

    public void setData(DataOneNews data) {
        this.data = data;
    }
}
