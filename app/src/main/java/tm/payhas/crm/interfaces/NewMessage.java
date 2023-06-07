package tm.payhas.crm.interfaces;

import android.view.View;

import tm.payhas.crm.dataModels.DataMessageTarget;

public interface NewMessage {
    void onNewMessage(DataMessageTarget dataMessageTarget);

    void deleteMessage(DataMessageTarget dataMessageTarget);

    void onMenuSelected(View view, Integer i, Integer messageId, DataMessageTarget messageTarget);
}
