package tm.payhas.crm.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class FcmPreferences {
    private final SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final Context _context;
    int PRIVATE_MODE = 0;
    private static FcmPreferences fcmPreferences;
    private static final String PREF_NAME = "_fcm_preference";
    public static final String FCM_TOKEN = "_fcm";
    public static final String IS_SENT = "_fcm_is_sent";


    public static FcmPreferences newInstance(Context context) {
        if (fcmPreferences == null) {
            fcmPreferences = new FcmPreferences(context);
        }
        return fcmPreferences;
    }

    public FcmPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFcm(String lang) {
        editor.putString(FCM_TOKEN, lang);
        editor.commit();
    }

    public void setIsSent(boolean isSent) {
        editor.putBoolean(IS_SENT, isSent);
        editor.commit();
    }

    public boolean getIsSent() {
        if (_context == null) {
            return false;
        } else
            return pref.getBoolean(IS_SENT, false);
    }


    public String getFcm() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(FCM_TOKEN, "");
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
