package tm.payhas.crm.domain.useCases;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.data.localdb.repository.RepositoryUser;

public class UseCaseUsers {

    public RepositoryUser repositoryUser;
    private Context context;

    public UseCaseUsers(Context context) {
        this.context = context;
        repositoryUser = new RepositoryUser(context);
    }

    public LiveData<List<EntityUserInfo>> getAllUsers() {
        return repositoryUser.getUsers();
    }

    public void connectAndUpdate() {
        repositoryUser.fetchAndInsertUsers();
    }

    public void addNewMessageCount(int userId) {
        repositoryUser.addNewMessageCount(userId);
    }

    public EntityUserInfo getOneUserInfo(int userId) {
        return repositoryUser.getOneUser(userId);
    }

    public void insertUser(EntityUserInfo user) {
        repositoryUser.insertUser(user);
    }

    public void deleteUser(EntityUserInfo user) {
        repositoryUser.deleteUser(user);
    }

    public void updateUser(EntityUserInfo user) {
        repositoryUser.updateUser(user);
    }

    public void deleteAllUsers() {
        repositoryUser.deleteAllUsers();
    }

    public void updateUserStatus(int userId, boolean isActive, String lastActivity) {
        repositoryUser.updateUserActivity(userId, isActive, lastActivity);
    }

    public void resetMessageCountForUser(int userId) {
        repositoryUser.resetMessageCountForUser(userId);
    }

    public void setMessageRoomText(String text, int userId, Integer roomId, String createdAt) {
        repositoryUser.setMessageRoomText(text, userId, roomId, createdAt);
        Log.e("UPDATE ROOM", "setMessageRoomText: " + text + "/" + userId + "/" + roomId + "/" + createdAt);
    }

    public LiveData<Integer> getRoomIdForUser(int userId) {
        return repositoryUser.getRoomIdByUserId(userId);
    }
}