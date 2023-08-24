package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataTaskMembers;

public class ResponseTaskMembers extends Response {
    private DataTaskMembers data;

    public DataTaskMembers getData() {
        return data;
    }

    public void setData(DataTaskMembers data) {
        this.data = data;
    }
}
