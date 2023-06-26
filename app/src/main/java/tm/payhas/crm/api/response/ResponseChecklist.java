package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataChecklist;

public class ResponseChecklist extends Response {
    private DataChecklist data;

    public DataChecklist getData() {
        return data;
    }

    public void setData(DataChecklist data) {
        this.data = data;
    }
}
