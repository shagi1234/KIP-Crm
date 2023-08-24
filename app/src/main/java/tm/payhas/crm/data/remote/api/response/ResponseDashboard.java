package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataDashboard;

public class ResponseDashboard extends Response {
    private DataDashboard data;

    public DataDashboard getData() {
        return data;
    }

    public void setData(DataDashboard data) {
        this.data = data;
    }
}
