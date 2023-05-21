package tm.payhas.crm.interfaces;

import tm.payhas.crm.dataModels.DataMessageTarget;

public interface NewMessage {
    void onNewMessage(DataMessageTarget dataMessageTarget);
}
