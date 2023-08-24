package tm.payhas.crm.data.localdb.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import tm.payhas.crm.data.localdb.dao.DaoGroup;
import tm.payhas.crm.data.localdb.dao.DaoMessage;
import tm.payhas.crm.data.localdb.dao.DaoUser;
import tm.payhas.crm.data.localdb.entity.EntityGroup;
import tm.payhas.crm.data.localdb.entity.EntityMessage;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;

@Database(entities = {EntityMessage.class, EntityGroup.class, EntityUserInfo.class}, version = 1)
public abstract class MessagesDatabase extends RoomDatabase {
    public abstract DaoMessage messageDao();

    public abstract DaoUser userDao();

    public abstract DaoGroup groupDao();
    

    private static volatile MessagesDatabase INSTANCE;

    public static MessagesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MessagesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MessagesDatabase.class, "app_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
