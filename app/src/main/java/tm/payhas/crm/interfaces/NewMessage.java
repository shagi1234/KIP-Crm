package tm.payhas.crm.interfaces;

import android.view.View;

import tm.payhas.crm.dataModels.DataMessageTarget;

public interface NewMessage {
    void onNewMessage(int type, DataMessageTarget dataMessageTarget);

    void deleteMessage(DataMessageTarget dataMessageTarget);

    void onMenuSelected(View view, Integer i, Integer messageId, DataMessageTarget messageTarget);

    void onMessageStatus(DataMessageTarget messageTarget);

    void onReceiveYourMessage(DataMessageTarget messageTarget);

}
