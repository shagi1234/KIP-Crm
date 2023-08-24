package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.domain.model.DataTask;

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
