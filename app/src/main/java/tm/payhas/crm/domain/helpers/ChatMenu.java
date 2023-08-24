package tm.payhas.crm.domain.helpers;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.presentation.viewModel.ViewModelChatRoom;

public class ChatMenu {
    private Context context;
    PopupMenu popupMenu = null;
    private int selectedInt;
    ViewModelChatRoom viewModelChatRoom;
    private EntityMessage messageTarget;

    public ChatMenu(Context context, ViewModelChatRoom viewModelChatRoom) {
        this.context = context;
        this.viewModelChatRoom = viewModelChatRoom;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void createPopUpMenu(View view, boolean isSent, EntityMessage message) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            popupMenu = new PopupMenu(context, view, R.style.PopupMenu);
        } else {
            popupMenu = new PopupMenu(context, view);
        }
        if (isSent) {
            popupMenu.getMenuInflater().inflate(R.menu.message_menu, popupMenu.getMenu());
        } else {
            popupMenu.getMenuInflater().inflate(R.menu.message_menu_received, popupMenu.getMenu());
        }
        popupMenu.setForceShowIcon(true);
        popupMenu.setGravity(Gravity.CLIP_HORIZONTAL);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.copy:

                    return true;
                case R.id.delete:
                    viewModelChatRoom.deleteMessage(message.getId());
                    return true;
                case R.id.reply:
                    viewModelChatRoom.setIsReply(true, message, message.getId());
                    return true;
            }
            return true;
        });
        popupMenu.show();
    }

    public void showMenu(EntityMessage message, View view, boolean isSent) {
        this.messageTarget = message;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            createPopUpMenu(view, isSent, message);
        }

    }

}
