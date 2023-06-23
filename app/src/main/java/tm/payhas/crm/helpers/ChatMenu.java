package tm.payhas.crm.helpers;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.RequiresApi;

import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterSingleChat;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.interfaces.NewMessage;

public class ChatMenu {
    private Context context;
    private int selectedMenu = 0;
    private AdapterSingleChat adapterSingleChat;

    public ChatMenu(Context context) {
        this.context = context;
        adapterSingleChat = new AdapterSingleChat(context, 0, 1);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void createPopUpMenu(View view, Integer messageId, DataMessageTarget messageTarget) {
        PopupMenu popupMenu = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            popupMenu = new PopupMenu(context, view, R.style.PopupMenu);
        } else {
            popupMenu = new PopupMenu(context, view);
        }
        popupMenu.getMenuInflater().inflate(R.menu.message_menu, popupMenu.getMenu());
        popupMenu.setForceShowIcon(true);
        popupMenu.setGravity(Gravity.CLIP_HORIZONTAL);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.copy:
                    selectedMenu = 1;
                    if (adapterSingleChat != null) {
                        ((NewMessage) adapterSingleChat).onMenuSelected(view, 1, messageId, messageTarget);
                    }
                    return true;
                case R.id.delete:
                    selectedMenu = 2;
                    if (adapterSingleChat != null) {
                        ((NewMessage) adapterSingleChat).onMenuSelected(view, 2, messageId, messageTarget);
                    }
                    return true;
                case R.id.reply:
                    selectedMenu = 3;
                    if (adapterSingleChat != null) {
                        ((NewMessage) adapterSingleChat).onMenuSelected(view, 3, messageId, messageTarget);
                    }
                    return true;
            }
            return true;
        });
        popupMenu.show();
    }

    public int getSelectedMenu() {
        return selectedMenu;
    }
}
