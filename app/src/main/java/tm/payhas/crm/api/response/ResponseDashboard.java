package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataDashboard;

public class ResponseDashboard extends Response {
    private DataDashboard data;

    public DataDashboard getData() {
        return data;
    }

    public void setData(DataDashboard data) {
        this.data = data;
    }
}
