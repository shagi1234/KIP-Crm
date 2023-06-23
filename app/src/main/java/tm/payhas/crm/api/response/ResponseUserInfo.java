package tm.payhas.crm.api.response;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class ResponseUserInfo extends Response {
    public DtoUserInfo data;

    public DtoUserInfo getData() {
        return data;
    }

    public void setData(DtoUserInfo data) {
        this.data = data;
    }
}
