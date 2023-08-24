package tm.payhas.crm.data.remote.repository;

import static tm.payhas.crm.domain.helpers.StaticMethods.getCurrentTime;
import static tm.payhas.crm.domain.statics.StaticConstants.CREATE_MESSAGE;
import static tm.payhas.crm.domain.statics.StaticConstants.FILE;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_SENDING;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_SENT;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_UN_SEND;
import static tm.payhas.crm.domain.statics.StaticConstants.NEW_ROOM;
import static tm.payhas.crm.domain.statics.StaticConstants.PHOTO;
import static tm.payhas.crm.domain.statics.StaticConstants.VOICE;
import static tm.payhas.crm.presentation.view.activity.ActivityMain.webSocket;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;
import tm.payhas.crm.data.localdb.repository.RepositoryMessage;
import tm.payhas.crm.data.remote.api.request.RequestNewMessage;
import tm.payhas.crm.data.remote.api.response.ResponseSingleFile;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.domain.helpers.FileUtil;
import tm.payhas.crm.domain.model.DataAnswer;
import tm.payhas.crm.domain.model.DataAttachment;
import tm.payhas.crm.domain.model.DataAuthor;
import tm.payhas.crm.domain.model.DataFile;
import tm.payhas.crm.domain.model.DataPersonalData;
import tm.payhas.crm.domain.model.DataUploadFileResult;

public class RepositorySendMessage {
    private final String TAG = "RepositorySendMessage";
    private final RepositoryMessage chatRoomRepository;
    private final AccountPreferences accountPreferences;

    public RepositorySendMessage(RepositoryMessage chatRoomRepository, Context context) {
        this.chatRoomRepository = chatRoomRepository;
        accountPreferences = AccountPreferences.newInstance(context);
    }

    public void sendMessage(int replyId, String text, String messageType, DataAnswer messageToReply, int roomId, int userId) {
        EntityMessage newMessageData = createNewMessage(replyId, text, messageType, messageToReply, roomId, userId);
        RequestNewMessage newMessage = createRequestNewMessage(messageType, newMessageData);
        newMessage.setEvent(CREATE_MESSAGE);
        newMessage.setData(newMessageData);
        String s = new Gson().toJson(newMessage);
        webSocket.sendMessage(s);
        Log.e(TAG, "sendMessage: "+s );
        chatRoomRepository.insert(newMessageData);
    }

    public void sendFile(String messageType, DataFile dataFile, int roomId, int userId, int replyId, DataAnswer messageToReply) {
        EntityMessage newMessageData = createNewMessage(replyId, "", messageType, messageToReply, roomId, userId);
        newMessageData.setAttachment(createDataAttachment(dataFile));

        RequestNewMessage newMessage = createRequestNewMessage(messageType, newMessageData);
        newMessage.setEvent(CREATE_MESSAGE);
        newMessage.setData(newMessageData);
        String s = new Gson().toJson(newMessage);
        webSocket.sendMessage(s);
        chatRoomRepository.insert(newMessageData);
    }

    public void sendVoiceMessage(DataFile dataFile, int roomId, int userId, int replyId, DataAnswer messageToReply) {
        sendFile(VOICE, dataFile, roomId, userId, replyId, messageToReply);
    }

    public void sendImageMessage(DataFile dataFile, int roomId, int userId, int replyId, DataAnswer messageToReply) {
        sendFile(PHOTO, dataFile, roomId, userId, replyId, messageToReply);
    }

    private EntityMessage createNewMessage(int replyId, String text, String messageType, DataAnswer messageToReply, int roomId, int userId) {
        EntityMessage newMessageData = new EntityMessage();
        newMessageData.setRoomId(roomId);
        newMessageData.setStatus(MESSAGE_SENDING);
        newMessageData.setAuthor(setUserInfo());
        newMessageData.setType(messageType);
        newMessageData.setAuthorId(accountPreferences.getAuthorId());
        newMessageData.setText(text);
        newMessageData.setFriendId(userId);
        newMessageData.setCreatedAt(getCurrentTime());
        newMessageData.setLocalId(UUID.randomUUID().toString());

        if (replyId != 0) {
            newMessageData.setAnswerId(replyId);
            newMessageData.setAnswering(messageToReply);
        }

        return newMessageData;
    }

    private DataAuthor setUserInfo() {
        DataAuthor userInfo = new DataAuthor();
        DataPersonalData personalData = new DataPersonalData();
        personalData.setName(accountPreferences.getUserName());
        personalData.setSurname(accountPreferences.getPrefSurname());
        personalData.setBirthday(accountPreferences.getPrefBirthday());
        personalData.setLastName(accountPreferences.getPrefLastname());
        userInfo.setPersonalDataAuthor(personalData);
        return userInfo;
    }

    private RequestNewMessage createRequestNewMessage(String messageType, EntityMessage newMessageData) {
        RequestNewMessage newMessage = new RequestNewMessage();
        newMessage.setData(newMessageData);
        return newMessage;
    }

    private DataAttachment createDataAttachment(DataFile dataFile) {
        DataAttachment dataAttachment = new DataAttachment();
        dataAttachment.setFileUrl(dataFile.getUrl());
        dataAttachment.setSize(dataFile.getSize());
        dataAttachment.setFileName(dataFile.getOriginalFileName());
        Log.e(TAG, "createDataAttachment: " + dataFile.getOriginalFileName());
        return dataAttachment;
    }

    public DataUploadFileResult uploadFile(String messageType, File file, int replyId, String text, DataAnswer messageToReply, int roomId, int userId) {
        new UploadFileAsyncTask(messageType, file, replyId, text, messageToReply, roomId, userId).execute();
        // You can return a placeholder result for now, as the actual result will be delivered asynchronously
        return new DataUploadFileResult(messageType, new DataFile());
    }

    private class UploadFileAsyncTask extends AsyncTask<Void, Void, DataUploadFileResult> {
        private String messageType;
        private File file;
        private int replyId;
        private String text;
        private DataAnswer messageToReply;
        private int roomId;
        private int userId;

        public UploadFileAsyncTask(String messageType, File file, int replyId, String text, DataAnswer messageToReply, int roomId, int userId) {
            this.messageType = messageType;
            this.file = file;
            this.replyId = replyId;
            this.text = text;
            this.messageToReply = messageToReply;
            this.roomId = roomId;
            this.userId = userId;
        }

        @Override
        protected DataUploadFileResult doInBackground(Void... voids) {
            // Perform network operations here
            try {
                MultipartBody.Part fileToUpload = null;
                RequestBody requestFile = RequestBody.create(MediaType.parse(FileUtil.getMimeType(file)), file);

                try {
                    fileToUpload = MultipartBody.Part.createFormData("fileUrl", URLEncoder.encode(file.getPath(), "utf-8"), requestFile);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                RequestBody originalFileName = RequestBody.create(MediaType.parse("multipart/form-data"), file.getName());

                Call<ResponseSingleFile> upload = Common.getApi().uploadFile(originalFileName, fileToUpload);
                Response<ResponseSingleFile> response = upload.execute();
                DataFile fileUrl = new DataFile();
                if (response.isSuccessful()) {
                    fileUrl.setSize(response.body().getData().getSize());
                    fileUrl.setUrl(response.body().getData().getUrl());
                    fileUrl.setOriginalFileName(response.body().getData().getOriginalFileName());
                }

                switch (messageType) {
                    case PHOTO:
                        sendImageMessage(fileUrl, roomId, userId, replyId, messageToReply);
                        break;
                    case VOICE:
                        sendVoiceMessage(fileUrl, roomId, userId, replyId, messageToReply);
                        break;
                    case FILE:
                        sendFile(FILE, fileUrl, roomId, userId, replyId, messageToReply);
                        break;
                }

                return new DataUploadFileResult(messageType, fileUrl);
            } catch (IOException e) {
                Log.e(TAG, "Error uploading file: " + e.getMessage());
                return new DataUploadFileResult("", new DataFile());
            }
        }

        @Override
        protected void onPostExecute(DataUploadFileResult result) {
            // This method is executed on the UI thread after doInBackground is finished
            // You can notify the UI or perform any required actions with the result here
        }
    }
}
