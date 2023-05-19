package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.dataModels.DataTask;

public class ResponseTasks extends Response {
    private ResponseUserTasks data;
    private DataTask task;

    public ResponseUserTasks getData() {
        return data;
    }

    public void setData(ResponseUserTasks data) {
        this.data = data;
    }

}
