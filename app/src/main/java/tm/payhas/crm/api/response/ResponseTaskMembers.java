package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataTaskMembers;

public class ResponseTaskMembers extends Response {
    private DataTaskMembers data;

    public DataTaskMembers getData() {
        return data;
    }

    public void setData(DataTaskMembers data) {
        this.data = data;
    }
}
