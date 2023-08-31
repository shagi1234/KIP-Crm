package tm.payhas.crm.data.localdb.repository;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static tm.payhas.crm.domain.helpers.StaticMethods.getCurrentTime;
import static tm.payhas.crm.domain.statics.StaticConstants.MESSAGE_UN_SEND;

import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import tm.payhas.crm.data.remote.api.response.ResponseRoomMessages;
import tm.payhas.crm.data.localdb.dao.DaoMessage;
import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.data.localdb.room.MessagesDatabase;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class RepositoryMessage {

    private final DaoMessage daoMessage;
    private final Context context;
    int roomId;
    int page = 1;
    int limit = 20;

    public RepositoryMessage(int roomId, Context context) {
        MessagesDatabase messagesDatabase = MessagesDatabase.getInstance(context);
        this.context = context;
        this.roomId = roomId;
        daoMessage = messagesDatabase.messageDao();
    }

    private Timer timer;


    public void startUpdatingSendingMessages() {
        timer = new Timer();
        timer.schedule(new UpdateSendingMessagesTask(), 5000, 10000);// Call the method every 5 seconds
        Log.e(TAG, "startUpdatingSendingMessages: looping");
    }

    public void stopUpdatingSendingMessages() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.e(TAG, "stopUpdatingSendingMessages: stopped");
        }
    }

    private class UpdateSendingMessagesTask extends TimerTask {
        @Override
        public void run() {
            checkAndUpdateSendingMessages();
            if (!areSendingMessagesPresent()) {
                stopUpdatingSendingMessages();
            }
        }
    }

    public void insertAll(List<EntityMessage> messages) {
        new InsertMessagesAsyncTask(daoMessage).execute(messages);
    }

    public void delete(EntityMessage message) {
        new DeleteMessageAsyncTask(daoMessage).execute(message);
    }

    public void update(EntityMessage message) {
        new UpdateMessageAsyncTask(daoMessage).execute(message);
    }

    public void insert(EntityMessage message) {
        new InsertMessageAsyncTask(daoMessage).execute(message);
        startUpdatingSendingMessages();
    }


    public void deleteAll() {
        new DeleteAllMessagesAsyncTask(daoMessage).execute();
    }

    private static class InsertMessagesAsyncTask extends AsyncTask<List<EntityMessage>, Void, Void> {
        private DaoMessage daoMessage;

        InsertMessagesAsyncTask(DaoMessage dao) {
            daoMessage = dao;
        }

        @Override
        protected Void doInBackground(List<EntityMessage>... messages) {
            daoMessage.insertAll(messages[0]);
            return null;
        }
    }


    public void checkAndUpdateSendingMessages() {
        if (!areSendingMessagesPresent()) {
            return;
        }
        List<EntityMessage> sendingMessages = daoMessage.getAndCheckSendingMessages();
        Log.e(TAG, "checkAndUpdateSendingMessages: Checking sending messages" + sendingMessages.size());
        if (sendingMessages.isEmpty()) {
            stopUpdatingSendingMessages(); // Stop updating loop when no more sending messages
            Log.e(TAG, "checkAndUpdateSendingMessages: stopped by no messages sending");
            return;
        }
        for (EntityMessage message : sendingMessages) {
            Log.e(TAG, "checkAndUpdateSendingMessages: " + message.getId());
            String currentTime = getCurrentTime();
            String messageCreatedAt = message.getCreatedAt();

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                Date currentDateTime = sdf.parse(currentTime);
                Date messageCreatedAtDateTime = sdf.parse(messageCreatedAt);

                long elapsedTime = currentDateTime.getTime() - messageCreatedAtDateTime.getTime();

                if (elapsedTime >= 10000) { // Replace with your time threshold
                    message.setStatus(MESSAGE_UN_SEND);
                    Log.e(TAG, "checkAndUpdateSendingMessages: updated message" + message.getId() + message.getStatus());
                    update(message);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public LiveData<Boolean> checkDoesRoomExist(int roomId) {
        MutableLiveData<Boolean> roomExistsLiveData = new MutableLiveData<>();

        AsyncTask.execute(() -> {
            int messageCount = daoMessage.getRoomMessages(roomId).getValue().size();
            boolean roomExists = messageCount != 0;
            roomExistsLiveData.postValue(roomExists);
        });

        return roomExistsLiveData;
    }


    private static class DeleteMessageAsyncTask extends AsyncTask<EntityMessage, Void, Void> {
        private DaoMessage daoMessage;

        DeleteMessageAsyncTask(DaoMessage dao) {
            daoMessage = dao;
        }

        @Override
        protected Void doInBackground(EntityMessage... messages) {
            daoMessage.delete(messages[0]);
            return null;
        }
    }


    private static class InsertMessageAsyncTask extends AsyncTask<EntityMessage, Void, Void> {
        private DaoMessage daoMessage;

        InsertMessageAsyncTask(DaoMessage dao) {
            daoMessage = dao;
        }

        @Override
        protected Void doInBackground(EntityMessage... messages) {
            EntityMessage message = messages[0];
            daoMessage.insert(message);
            return null;
        }
    }

    private static class UpdateMessageAsyncTask extends AsyncTask<EntityMessage, Void, Void> {
        private DaoMessage daoMessage;

        UpdateMessageAsyncTask(DaoMessage dao) {
            daoMessage = dao;
        }

        @Override
        protected Void doInBackground(EntityMessage... messages) {
            EntityMessage message = messages[0];
            daoMessage.update(message); // Assuming you have an update method in DaoMessage
            return null;
        }
    }

    private static class DeleteAllMessagesAsyncTask extends AsyncTask<Void, Void, Void> {
        private DaoMessage daoMessage;

        DeleteAllMessagesAsyncTask(DaoMessage dao) {
            daoMessage = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            daoMessage.deleteAll();
            return null;
        }
    }

    public void loadNextPage() {
        // Calculate the next page number
        page++;

        // Call the modified getAllMessages method for the next page
        fetchAndInsertMessages(page);
    }

    public void fetchAndInsertMessages(int page) {
        if (roomId == 0) return;
        Log.e("FETCH_INSERT_MESSAGES", "fetchAndInsertMessages: ");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Call<ResponseRoomMessages> call = Common.getApi().getMessageRoom(AccountPreferences.newInstance(context).getToken(), roomId, page, limit);
                Response<ResponseRoomMessages> response = call.execute();

                if (response.isSuccessful()) {
                    List<EntityMessage> messagesFromApi = response.body().getData();

                    // Insert new groups into the local database
                    daoMessage.insertAll(messagesFromApi);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching and inserting groups: " + e.getMessage());
            }
        });
    }

    public LiveData<List<EntityMessage>> getMessages(int roomId) {
        fetchAndInsertMessages(1);
        return daoMessage.getRoomMessages(roomId);
    }

    private boolean areSendingMessagesPresent() {
        List<EntityMessage> sendingMessages = daoMessage.getAndCheckSendingMessages();
        return !sendingMessages.isEmpty();
    }

}

