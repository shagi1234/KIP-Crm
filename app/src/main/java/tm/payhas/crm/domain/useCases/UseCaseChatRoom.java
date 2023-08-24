package tm.payhas.crm.domain.useCases;

import static tm.payhas.crm.domain.statics.StaticConstants.CREATE_MESSAGE;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_RECEIVE;
import static tm.payhas.crm.presentation.view.activity.ActivityMain.webSocket;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;
import tm.payhas.crm.data.localdb.repository.RepositoryFile;
import tm.payhas.crm.data.localdb.repository.RepositoryMessage;
import tm.payhas.crm.data.localdb.repository.RepositoryUser;
import tm.payhas.crm.data.remote.api.request.RequestNewMessage;
import tm.payhas.crm.data.remote.api.request.RequestReceivedMessage;
import tm.payhas.crm.data.remote.repository.RepositoryUserStatus;
import tm.payhas.crm.domain.model.DataUserStatus;

public class UseCaseChatRoom {
    public RepositoryMessage repositoryMessage;
    RepositoryFile repositoryFile;
    RepositoryUserStatus repositoryUserStatus;
    int roomId;
    private static final String TAG = "USE_CASE_CHATROOM";
    private Context context;
    private RepositoryUser repositoryUser;
    private static UseCaseChatRoom instance;

    public UseCaseChatRoom(Context context, int roomId) {
        this.roomId = roomId;
        this.context = context;
        repositoryMessage = new RepositoryMessage(roomId, context);
        repositoryFile = new RepositoryFile(context);
        repositoryUserStatus = new RepositoryUserStatus();
        repositoryUser = new RepositoryUser(context);
    }

    public static synchronized UseCaseChatRoom getInstance(Context context, int roomId) {
        if (instance == null) {
            Log.e(TAG, "instance = null");
            instance = new UseCaseChatRoom(context, roomId);
        }
        return instance;
    }

    public LiveData<Boolean> checkDoesRoomExist(int roomId) {
        return repositoryMessage.checkDoesRoomExist(roomId);
    }

    public void readMessage(EntityMessage entityMessage) {
        Log.e(TAG, "readMessage: " + roomId + entityMessage.getRoomId());
        if (roomId == entityMessage.getRoomId()) {
            RequestReceivedMessage requestNewMessage = new RequestReceivedMessage();
            requestNewMessage.setEvent(MESSAGE_RECEIVE);
            RequestReceivedMessage.MessageId messageId = new RequestReceivedMessage.MessageId();
            messageId.setMessageId(entityMessage.getId());
            requestNewMessage.setData(messageId);
            String s = new Gson().toJson(requestNewMessage);
            webSocket.sendMessage(s);
            Log.e(TAG, "readMessage: " + s);
            repositoryUser.resetMessageCountForUser(entityMessage.getAuthorId());
        }
    }

    public LiveData<List<EntityMessage>> getAllMessages(int roomId) {
        return repositoryMessage.getMessages(roomId);
    }

    public void loadNextPage() {
        repositoryMessage.loadNextPage();
    }

    public void insertMessage(EntityMessage message) {
        repositoryMessage.insert(message);
    }

    public void deleteMessage(EntityMessage message) {
        repositoryMessage.delete(message);
    }

    public void deleteAllMessages() {
        repositoryMessage.deleteAll();
    }

    public void insertAll(List<EntityMessage> messages) {
        repositoryMessage.insertAll(messages);
    }

    public File processFile(Uri uri) {
        return repositoryFile.processFile(uri);
    }

    public void setUserOnline() {
        DataUserStatus userStatus = new DataUserStatus();
        userStatus.setActive(true);
        userStatus.setUserId(AccountPreferences.newInstance(context).getAuthorId());
        repositoryUserStatus.setUserOnline(userStatus);
    }

    public void showDialog(Context context, EntityMessage message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Couldn't send message");
        builder.setMessage("Select one option");

        builder.setPositiveButton("Resend", (dialog, which) -> {
            // Do nothing but close the dialog
            RequestNewMessage newMessage = new RequestNewMessage();
            newMessage.setData(message);
            newMessage.setEvent(CREATE_MESSAGE);
            String s = new Gson().toJson(newMessage);
            webSocket.sendMessage(s);
            insertMessage(message);
            dialog.dismiss();
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            deleteMessage(message);
            dialog.dismiss();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
