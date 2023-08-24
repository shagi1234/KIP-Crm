package tm.payhas.crm.data.remote.api.response;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public class ResponseSignIn extends Response {
    private EntityUserInfo data;

    public EntityUserInfo getData() {
        return data;
    }

}
