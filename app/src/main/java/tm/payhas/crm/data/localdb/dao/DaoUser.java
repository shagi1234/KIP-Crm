package tm.payhas.crm.data.localdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

@Dao
public interface DaoUser {

    @Insert
    void insert(EntityUserInfo user);

    @Delete
    void delete(EntityUserInfo user);

    @Update
    void update(EntityUserInfo user);

    @Query("DELETE FROM table_users")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EntityUserInfo> group);

    @Query("SELECT * FROM table_users ")
    LiveData<List<EntityUserInfo>> getUsers();

    @Query("SELECT * FROM table_users WHERE id = :userId ")
    EntityUserInfo getOneUser(int userId);

    @Query("UPDATE table_users SET isActive = :isActive, lastActivity = :lastActivity WHERE id = :userId")
    void updateUserActivity(int userId, boolean isActive, String lastActivity);

    @Query("UPDATE table_users SET messages = messages + 1 WHERE id = :userId")
    void incrementMessageCountForUser(int userId);

    @Query("UPDATE table_users SET messages = 0 WHERE id = :userId")
    void resetMessageCountForUser(int userId);

    @Query("UPDATE table_users SET textRoom = :text WHERE id = :userId")
    void resetMessageCountForUser(String text, int userId);

    @Query("UPDATE table_users SET roomIdRoom = :roomId WHERE id = :userId")
    void updateRoomIdForUser(int roomId, int userId);

    @Query("UPDATE table_users SET createdAtRoom = :createdAt WHERE id = :userId")
    void updateCreatedAtRoom(String createdAt, int userId);

    @Query("SELECT roomIdRoom FROM table_users WHERE id = :userId")
    LiveData<Integer> getRoomIdByUserId(int userId);

    @Query("SELECT roomIdRoom FROM table_users WHERE id = :userId")
    Integer getRoomIdByUser(int userId);

}
