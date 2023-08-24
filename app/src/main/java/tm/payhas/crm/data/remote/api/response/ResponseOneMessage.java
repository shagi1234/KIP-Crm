package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.data.localdb.entity.EntityMessage;

public class ResponseOneMessage extends Response {
    private EntityMessage data;

    public EntityMessage getData() {
        return data;
    }

    public void setData(EntityMessage data) {
        this.data = data;
    }
}
