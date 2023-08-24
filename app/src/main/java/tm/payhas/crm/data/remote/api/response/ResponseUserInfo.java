package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public class ResponseUserInfo extends Response {
    public EntityUserInfo data;

    public EntityUserInfo getData() {
        return data;
    }

    public void setData(EntityUserInfo data) {
        this.data = data;
    }
}
