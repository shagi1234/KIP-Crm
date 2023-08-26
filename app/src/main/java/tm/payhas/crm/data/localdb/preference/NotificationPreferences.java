package tm.payhas.crm.data.localdb.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NotificationPreferences {

    private static final String PREF_KEY_NOTIFICATION_ENABLED = "notification_enabled";

    public static boolean isNotificationEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREF_KEY_NOTIFICATION_ENABLED, true); // Default is enabled
    }

    public static void setNotificationEnabled(Context context, boolean enabled) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(PREF_KEY_NOTIFICATION_ENABLED, enabled).apply();
    }
}
