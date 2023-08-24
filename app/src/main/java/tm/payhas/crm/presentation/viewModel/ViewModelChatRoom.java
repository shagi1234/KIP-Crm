package tm.payhas.crm.presentation.viewModel;

import static tm.payhas.crm.domain.helpers.StaticMethods.setDataAnswering;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import tm.payhas.crm.data.localdb.entity.EntityGroup;
import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.data.localdb.repository.RepositoryMessage;
import tm.payhas.crm.domain.model.DataAnswer;
import tm.payhas.crm.domain.model.DataFile;
import tm.payhas.crm.domain.useCases.UseCaseChatRoom;
import tm.payhas.crm.domain.useCases.UseCaseDeleteMessage;
import tm.payhas.crm.domain.useCases.UseCaseGroup;
import tm.payhas.crm.domain.useCases.UseCaseRecordVoice;
import tm.payhas.crm.domain.useCases.UseCaseSendMessage;
import tm.payhas.crm.domain.useCases.UseCaseUsers;

public class ViewModelChatRoom extends ViewModel {
    private static final String TAG = "ViewModelChatRoom";
    public UseCaseChatRoom useCaseChatRoom;
    private UseCaseSendMessage useCaseSendMessage;
    private RepositoryMessage repositoryMessage;
    private UseCaseRecordVoice useCaseRecordVoice;
    private MutableLiveData<Boolean> isReplyLiveData = new MutableLiveData<>();
    private DataAnswer messageToReply;
    private int replyId;
    private UseCaseDeleteMessage useCaseDeleteMessage;
    private MutableLiveData<String> currentMessagesDate = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAtLastPosition = new MutableLiveData<>(false);
    private UseCaseUsers useCaseUsers;
    private UseCaseGroup useCaseGroup;

    public ViewModelChatRoom(Context context, int roomId) {
        useCaseChatRoom = new UseCaseChatRoom(context, roomId);
        repositoryMessage = new RepositoryMessage(roomId, context);
        useCaseSendMessage = new UseCaseSendMessage(context, repositoryMessage);
        useCaseRecordVoice = new UseCaseRecordVoice(context);
        useCaseUsers = new UseCaseUsers(context);
        useCaseGroup = new UseCaseGroup(context);
        useCaseDeleteMessage = new UseCaseDeleteMessage(context);
        setUserStatus();
    }

    public LiveData<Integer> getRoomIdForUser(int userId) {
        return useCaseUsers.getRoomIdForUser(userId);
    }


    public void closeUseCase(Context context) {
        useCaseChatRoom = new UseCaseChatRoom(context, 0);
    }


    public EntityUserInfo getUserInfo(int userId) {
        return useCaseUsers.getOneUserInfo(userId);
    }

    public void resetMessageCountForUser(int userId) {
        useCaseUsers.resetMessageCountForUser(userId);
    }

    public EntityGroup getGroupInfo(int groupId) {
        return useCaseGroup.getOneGroupInfo(groupId);
    }

    public LiveData<String> getCurrentMessagesDate() {
        return currentMessagesDate;
    }

    public void setCurrentMessagesDate(String date) {
        currentMessagesDate.setValue(date);
    }

    public LiveData<Boolean> getIsReplyLiveData() {
        return isReplyLiveData;
    }

    public DataAnswer getMessageToReply() {
        return messageToReply;
    }

    public void setIsReply(boolean isReply, EntityMessage messageToReply, int replyId) {
        this.messageToReply = setDataAnswering(messageToReply);
        this.replyId = replyId;
        isReplyLiveData.setValue(isReply);
        if (this.messageToReply != null);
    }


    public LiveData<List<EntityMessage>> getAllMessages(int roomId) {
        Log.e(TAG, "getAllMessages: ");
        return useCaseChatRoom.getAllMessages(roomId);
    }

    public void insertMessage(EntityMessage entityMessage) {
        useCaseChatRoom.insertMessage(entityMessage);
    }

    public void loadNextPage() {
        useCaseChatRoom.loadNextPage();
    }

    public File processFile(Uri uri) {
        return useCaseChatRoom.processFile(uri);
    }

    public void setUserStatus() {
        useCaseChatRoom.setUserOnline();
    }

    public void sendMessage(String text, String messageType, int roomId, int userId) {
        if (replyId == 0) {
            useCaseSendMessage.sendMessage(0, text, messageType, null, roomId, userId);
        } else {
            useCaseSendMessage.sendMessage(replyId, text, messageType, messageToReply, roomId, userId);
            replyId = 0;
            messageToReply = null;
            isReplyLiveData.setValue(false);
        }
    }

    public void sendFileImageVoice(String messageType, File file, int replyId, String text, DataAnswer messageToReply, int roomId, int userId) throws IOException {
        useCaseSendMessage.sendFileImageVoice(messageType, file, replyId, text, messageToReply, roomId, userId);
    }

    public void sendFileImageVoice(String messageType, DataFile file, int replyId, String text, DataAnswer messageToReply, int roomId, int userId) throws IOException {
        useCaseSendMessage.sendFileImageVoice(messageType, file, replyId, text, messageToReply, roomId, userId);
    }

    public void startRecording() throws IOException {
        useCaseRecordVoice.startRecording();
    }

    public File stopRecording() {
        return useCaseRecordVoice.stopRecording();
    }

    public void deleteMessage(int messageId) {
        useCaseDeleteMessage.deleteMessage(messageId);
    }

}