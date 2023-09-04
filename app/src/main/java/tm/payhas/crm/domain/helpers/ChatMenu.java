package tm.payhas.crm.domain.helpers;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.data.remote.api.network.Network.BASE_URL;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.presentation.viewModel.ViewModelChatRoom;

public class ChatMenu implements DownloadProgressListener {
    private Context context;
    PopupMenu popupMenu = null;
    ViewModelChatRoom viewModelChatRoom;
    ProgressBar progressBar;
    private DownloadReceiver downloadReceiver; // Declare a member variable for the receiver

    public ChatMenu(Context context, ViewModelChatRoom viewModelChatRoom) {
        this.context = context;
        this.viewModelChatRoom = viewModelChatRoom;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void unregisterDownloadReceiver() {
        if (downloadReceiver != null) {
            context.unregisterReceiver(downloadReceiver);
            downloadReceiver = null; // Set the receiver instance to null
        }
    }

    @SuppressLint("ShowToast")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void createPopUpMenu(View view, boolean isSent, EntityMessage message, boolean isFile) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            popupMenu = new PopupMenu(context, view, R.style.PopupMenu);
        } else {
            popupMenu = new PopupMenu(context, view);
        }
        int popupGravity = (isSent) ? Gravity.START : Gravity.END;
        popupMenu.setGravity(popupGravity);
        popupMenu.getMenuInflater().inflate(R.menu.message_menu, popupMenu.getMenu());
        MenuItem download = popupMenu.getMenu().findItem(R.id.download);
        MenuItem copy = popupMenu.getMenu().findItem(R.id.copy);
        MenuItem delete = popupMenu.getMenu().findItem(R.id.delete);
        delete.setVisible(isSent);
        download.setVisible(isFile);
        copy.setVisible(!isFile);

        popupMenu.setForceShowIcon(true);
        popupMenu.setGravity(Gravity.CLIP_HORIZONTAL);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.copy:
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    // Create a ClipData object to hold the text
                    ClipData clipData = ClipData.newPlainText("Message", message.getText());
                    // Set the data to the clipboard
                    clipboardManager.setPrimaryClip(clipData);
                    return true;
                case R.id.delete:
                    viewModelChatRoom.deleteMessage(message.getId());
                    return true;
                case R.id.download:
                    view.setVisibility(View.VISIBLE);
                    // Create and register the DownloadReceiver instance
                    downloadReceiver = new DownloadReceiver(progressBar, this);
                    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                    context.registerReceiver(downloadReceiver, filter);
                    FileDownloadManager.downloadFile(context, BASE_PHOTO + message.getAttachment().getFileUrl(), message.getAttachment().getFileName());
                    return true;
                case R.id.reply:
                    viewModelChatRoom.setIsReply(true, message, message.getId());
                    return true;
            }
            return true;
        });
        popupMenu.show();
    }

    public void showMenu(EntityMessage message, View view, boolean isSent, boolean isFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            createPopUpMenu(view, isSent, message, isFile);
        }

    }

    @Override
    public void onProgressUpdate(int progress) {
        progressBar.setProgress(progress);
        if (progress == 100) {
            progressBar.setVisibility(View.INVISIBLE);
            unregisterDownloadReceiver();
        }
    }
}
