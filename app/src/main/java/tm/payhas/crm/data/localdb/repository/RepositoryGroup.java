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
import tm.payhas.crm.data.localdb.dao.DaoUser;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.data.remote.api.response.ResponseUserGroup;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.data.localdb.dao.DaoGroup;
import tm.payhas.crm.data.localdb.entity.EntityGroup;
import tm.payhas.crm.data.localdb.room.MessagesDatabase;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class RepositoryGroup {

    private final DaoGroup daoGroup;
    private final Context context;
    public LiveData<List<EntityGroup>> allGroups;
    private final String TAG = "Repository_group";

    public RepositoryGroup(Context context) {
        this.context = context;
        MessagesDatabase messagesDatabase = MessagesDatabase.getInstance(context);
        daoGroup = messagesDatabase.groupDao();
        allGroups = getGroups();
        // Initialize allGroups using an empty list
    }

    public EntityGroup getOneGroup(int groupId) {
        try {
            return new GetGroupAsyncTask(daoGroup).execute(groupId).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertGroup(EntityGroup group) {
        new InsertAsyncTask(daoGroup).execute(group);
    }

    public void deleteGroup(EntityGroup group) {
        new DeleteAsyncTask(daoGroup).execute(group);
    }

    public void updateGroup(EntityGroup group) {
        new UpdateAsyncTask(daoGroup).execute(group);
    }

    public void deleteAllGroups() {
        new DeleteAllAsyncTask(daoGroup).execute();
    }

    public LiveData<List<EntityGroup>> getGroupsSorted() {
        LiveData<List<EntityGroup>> allGroupsLiveData = daoGroup.getAllGroups();

        LiveData<List<EntityGroup>> sortedGroupsLiveData = Transformations.map(allGroupsLiveData, allGroups -> {
            if (allGroups != null) {
                Collections.sort(allGroups, (group1, group2) -> group2.getMessageRoom().getCreatedAtRoom().compareTo(group1.getMessageRoom().getCreatedAtRoom()));
            }
            return allGroups;
        });

        return sortedGroupsLiveData;
    }

    public void fetchAndInsertGroups() {
        Log.e(TAG, "fetchAndInsertGroups: ");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Call<ResponseUserGroup> call = Common.getApi().getContacts(AccountPreferences.newInstance(context).getToken());
                Response<ResponseUserGroup> response = call.execute();

                if (response.isSuccessful()) {
                    List<EntityGroup> usersFromApi = response.body().getData().getGroups();

                    // Insert new groups into the local database
                    daoGroup.insertAll(usersFromApi);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching and inserting groups: " + e.getMessage());
            }
        });
    }

    public LiveData<List<EntityGroup>> getGroups() {
        return getGroupsSorted();
    }


    private static class InsertAllAsyncTask extends AsyncTask<List<EntityGroup>, Void, Void> {
        private DaoGroup daoGroup;

        InsertAllAsyncTask(DaoGroup dao) {
            daoGroup = dao;
        }

        @Override
        protected Void doInBackground(List<EntityGroup>... lists) {
            daoGroup.insertAll(lists[0]);
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<EntityGroup, Void, Void> {
        private DaoGroup daoGroup;

        InsertAsyncTask(DaoGroup dao) {
            daoGroup = dao;
        }

        @Override
        protected Void doInBackground(EntityGroup... groups) {
            daoGroup.insert(groups[0]);
            return null;
        }
    }

    private static class GetGroupAsyncTask extends AsyncTask<Integer, Void, EntityGroup> {
        private DaoGroup daoGroup;

        GetGroupAsyncTask(DaoGroup daoGroup) {
            this.daoGroup = daoGroup;
        }

        @Override
        protected EntityGroup doInBackground(Integer... groupIds) {
            int groupId = groupIds[0];
            return daoGroup.getOneGroup(groupId);
        }
    }


    private static class DeleteAsyncTask extends AsyncTask<EntityGroup, Void, Void> {
        private DaoGroup daoGroup;

        DeleteAsyncTask(DaoGroup dao) {
            daoGroup = dao;
        }

        @Override
        protected Void doInBackground(EntityGroup... groups) {
            daoGroup.delete(groups[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<EntityGroup, Void, Void> {
        private DaoGroup daoGroup;

        UpdateAsyncTask(DaoGroup dao) {
            daoGroup = dao;
        }

        @Override
        protected Void doInBackground(EntityGroup... groups) {
            daoGroup.update(groups[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private DaoGroup daoGroup;

        DeleteAllAsyncTask(DaoGroup dao) {
            daoGroup = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            daoGroup.deleteAll();
            return null;
        }
    }
}
