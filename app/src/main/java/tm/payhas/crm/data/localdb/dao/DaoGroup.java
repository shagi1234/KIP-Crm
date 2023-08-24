package tm.payhas.crm.data.localdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tm.payhas.crm.data.localdb.entity.EntityGroup;

@Dao
public interface DaoGroup {

    @Insert
    void insert(EntityGroup group);

    @Delete
    void delete(EntityGroup group);

    @Update
    void update(EntityGroup group);

    @Query("DELETE FROM table_groups")
    void deleteAll();

    @Query("SELECT * FROM table_groups WHERE id = :groupId")
    EntityGroup  getOneGroup(int groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EntityGroup> group);

    @Query("SELECT * FROM table_groups ORDER BY id")
    LiveData<List<EntityGroup>> getAllGroups();

}
