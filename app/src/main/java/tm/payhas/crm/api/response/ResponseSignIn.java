package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class ResponseSignIn extends Response {
    private DtoUserInfo data;

    public DtoUserInfo getData() {
        return data;
    }

}
