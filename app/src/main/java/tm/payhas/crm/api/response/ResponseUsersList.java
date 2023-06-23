package tm.payhas.crm.api.response;

import java.util.ArrayList;

import tm.payhas.crm.api.Response;
import tm.payhas.crm.api.data.dto.DtoUserInfo;

public class ResponseUsersList extends Response {
    private ArrayList<DtoUserInfo> data;

    public ArrayList<DtoUserInfo> getData() {
        return data;
    }

    public void setData(ArrayList<DtoUserInfo> data) {
        this.data = data;
    }
}
