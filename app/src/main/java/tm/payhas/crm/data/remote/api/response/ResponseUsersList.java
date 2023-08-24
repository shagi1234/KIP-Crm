package tm.payhas.crm.data.remote.api.response;

import java.util.ArrayList;

import tm.payhas.crm.data.remote.api.Response;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

public class ResponseUsersList extends Response {
    private ArrayList<EntityUserInfo> data;

    public ArrayList<EntityUserInfo> getData() {
        return data;
    }

    public void setData(ArrayList<EntityUserInfo> data) {
        this.data = data;
    }
}
