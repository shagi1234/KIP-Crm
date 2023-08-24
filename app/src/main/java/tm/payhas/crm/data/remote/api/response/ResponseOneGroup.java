package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.data.localdb.entity.EntityGroup;

public class ResponseOneGroup extends Response {
    private EntityGroup data;

    public EntityGroup getData() {
        return data;
    }

    public void setData(EntityGroup data) {
        this.data = data;
    }
}
