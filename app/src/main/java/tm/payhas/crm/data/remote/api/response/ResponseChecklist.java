package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataChecklist;

public class ResponseChecklist extends Response {
    private DataChecklist data;

    public DataChecklist getData() {
        return data;
    }

    public void setData(DataChecklist data) {
        this.data = data;
    }
}
