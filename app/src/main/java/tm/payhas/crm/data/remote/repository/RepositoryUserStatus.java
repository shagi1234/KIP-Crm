package tm.payhas.crm.data.remote.repository;

import static tm.payhas.crm.domain.statics.StaticConstants.USER_STATUS;
import static tm.payhas.crm.domain.statics.StaticConstants.USER_STATUS_CHANNEL;
import static tm.payhas.crm.presentation.view.activity.ActivityMain.webSocket;

import tm.payhas.crm.data.remote.webSocket.EmmitUserStatus;
import tm.payhas.crm.data.remote.webSocket.WebSocket;
import tm.payhas.crm.domain.model.DataUserStatus;

public class RepositoryUserStatus {
    public RepositoryUserStatus() {
    }

    public void setUserOnline(DataUserStatus userStatus) {
        EmmitUserStatus emitUserStatus = new EmmitUserStatus();
        emitUserStatus.setChannel(USER_STATUS_CHANNEL);
        emitUserStatus.setEvent(USER_STATUS);
        emitUserStatus.setData(userStatus);
        webSocket.setUserStatus(emitUserStatus);
    }
}