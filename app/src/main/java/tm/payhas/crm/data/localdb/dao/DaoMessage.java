package tm.payhas.crm.data.localdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tm.payhas.crm.data.localdb.entity.EntityMessage;

import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface DaoMessage {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EntityMessage> messages);

    @Delete
    void delete(EntityMessage message);

    @Update
    void update(EntityMessage message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EntityMessage message);

    @Query("DELETE FROM table_message")
    void deleteAll();

    @Query("SELECT * FROM table_message WHERE roomId = :roomId AND deletedAt IS NULL ORDER BY createdAt DESC")
    LiveData<List<EntityMessage>> getRoomMessages(int roomId);

    @Query("SELECT * FROM table_message WHERE status = 'status-1'")
    List<EntityMessage> getAndCheckSendingMessages();

}
