package tm.payhas.crm.data.localdb.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import tm.payhas.crm.data.remote.api.response.ResponseUserGroup;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.data.localdb.room.MessagesDatabase;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.data.localdb.dao.DaoUser;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class RepositoryUser {

    private final DaoUser userDao;
    private final Context context;
    private final String TAG = "Repository_user";


    public RepositoryUser(Context context) {
        MessagesDatabase messagesDatabase = MessagesDatabase.getInstance(context);
        userDao = messagesDatabase.userDao();
        this.context = context;
    }

    public EntityUserInfo getOneUser(int userId) {
        try {
            return new GetUserAsyncTask(userDao).execute(userId).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertUser(EntityUserInfo user) {
        new InsertAsyncTask(userDao).execute(user);
    }

    public void deleteUser(EntityUserInfo user) {
        new DeleteAsyncTask(userDao).execute(user);
    }

    public void updateUser(EntityUserInfo user) {
        new UpdateAsyncTask(userDao).execute(user);
    }

    public void deleteAllUsers() {
        new DeleteAllAsyncTask(userDao).execute();
    }

    public void updateUserActivity(int userId, boolean isActive, String lastActivity) {
        new UpdateUserActivityAsyncTask(userDao).execute(userId, isActive, lastActivity);
    }

    private static class UpdateUserActivityAsyncTask extends AsyncTask<Object, Void, Void> {
        private DaoUser daoUser;

        UpdateUserActivityAsyncTask(DaoUser dao) {
            daoUser = dao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            int userId = (int) params[0];
            boolean isActive = (boolean) params[1];
            String lastActivity = (String) params[2];

            daoUser.updateUserActivity(userId, isActive, lastActivity);
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<EntityUserInfo, Void, Void> {
        private DaoUser daoUser;

        InsertAsyncTask(DaoUser dao) {
            daoUser = dao;
        }

        @Override
        protected Void doInBackground(EntityUserInfo... users) {
            daoUser.insert(users[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<EntityUserInfo, Void, Void> {
        private DaoUser daoUser;

        DeleteAsyncTask(DaoUser dao) {
            daoUser = dao;
        }

        @Override
        protected Void doInBackground(EntityUserInfo... users) {
            daoUser.delete(users[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityUserInfo, Void, Void> {
        private DaoUser daoUser;

        UpdateAsyncTask(DaoUser dao) {
            daoUser = dao;
        }

        @Override
        protected Void doInBackground(EntityUserInfo... users) {
            daoUser.update(users[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private DaoUser daoUser;

        DeleteAllAsyncTask(DaoUser dao) {
            daoUser = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            daoUser.deleteAll();
            return null;
        }
    }

    public LiveData<List<EntityUserInfo>> getUsersSorted() {
        LiveData<List<EntityUserInfo>> allUsersLiveData = userDao.getUsers();

        LiveData<List<EntityUserInfo>> sortedUsersLiveData = Transformations.map(allUsersLiveData, allUsers -> {
            if (allUsers != null) {
                Collections.sort(allUsers, (group1, group2) -> group2.getMessageRoom().getCreatedAtRoom().compareTo(group1.getMessageRoom().getCreatedAtRoom()));
            }
            return allUsers;
        });

        return sortedUsersLiveData;
    }

    private static class GetUserAsyncTask extends AsyncTask<Integer, Void, EntityUserInfo> {
        private DaoUser userDao;

        GetUserAsyncTask(DaoUser userDao) {
            this.userDao = userDao;
        }

        @Override
        protected EntityUserInfo doInBackground(Integer... userIds) {
            int userId = userIds[0];
            return userDao.getOneUser(userId);
        }
    }

    public void fetchAndInsertUsers() {
        Log.e(TAG, "fetchAndInsertUsers()");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Call<ResponseUserGroup> call = Common.getApi().getContacts(AccountPreferences.newInstance(context).getToken());
                Response<ResponseUserGroup> response = call.execute();

                if (response.isSuccessful()) {
                    List<EntityUserInfo> usersFromApi = response.body().getData().getUsersPrivate();

                    // Insert new groups into the local database
                    userDao.insertAll(usersFromApi);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching and inserting groups: " + e.getMessage());
            }
        });
    }

    public LiveData<List<EntityUserInfo>> getUsers() {
        return getUsersSorted();
    }

    public void addNewMessageCount(int userId) {
        new IncrementMessageCountAsyncTask(userDao).execute(userId);
    }

    public void resetMessageCountForUser(int userId) {
        new ResetMessageCountAsyncTask(userDao).execute(userId);
    }

    private static class ResetMessageCountAsyncTask extends AsyncTask<Integer, Void, Void> {
        private DaoUser userDao;

        ResetMessageCountAsyncTask(DaoUser userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Integer... userIds) {
            int userId = userIds[0];
            userDao.resetMessageCountForUser(userId);
            return null;
        }
    }

    private static class IncrementMessageCountAsyncTask extends AsyncTask<Integer, Void, Void> {
        private DaoUser userDao;

        IncrementMessageCountAsyncTask(DaoUser userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Integer... userIds) {
            int userId = userIds[0];
            userDao.incrementMessageCountForUser(userId);
            return null;
        }
    }

    public void setMessageRoomText(String textRoom, int userId, Integer roomId, String createdAt) {
        setRoomIdForUser(roomId, userId);
        setCreatedAtForUser(createdAt, userId);
        new SetMessageRoomTextAsyncTask(userDao).execute(textRoom, userId);
    }

    private static class SetMessageRoomTextAsyncTask extends AsyncTask<Object, Void, Void> {
        private DaoUser userDao;

        SetMessageRoomTextAsyncTask(DaoUser userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            String textRoom = (String) params[0];
            int userId = (int) params[1];
            userDao.resetMessageCountForUser(textRoom, userId);
            return null;
        }
    }

    private static class SetRoomIdForUserAsyncTask extends AsyncTask<Object, Void, Void> {
        private DaoUser userDao;

        SetRoomIdForUserAsyncTask(DaoUser userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            int roomId = (int) params[0];
            int userId = (int) params[1];
            userDao.updateRoomIdForUser(roomId, userId);
            return null;
        }
    }

    private static class SetCreatedAtForUserAsyncTask extends AsyncTask<Object, Void, Void> {
        private DaoUser userDao;

        SetCreatedAtForUserAsyncTask(DaoUser userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            String createdAt = (String) params[0];
            int userId = (int) params[1];
            userDao.updateCreatedAtRoom(createdAt, userId);
            return null;
        }
    }

    public void setRoomIdForUser(int roomId, int userId) {
        if (userDao.getRoomIdByUser(userId) == 0 || userDao.getRoomIdByUser(userId) == null)
            new SetRoomIdForUserAsyncTask(userDao).execute(roomId, userId);
    }

    public void setCreatedAtForUser(String createdAt, int userId) {
        new SetCreatedAtForUserAsyncTask(userDao).execute(createdAt, userId);
    }

    public LiveData<Integer> getRoomIdByUserId(int userId) {
        return userDao.getRoomIdByUserId(userId);
    }

}
