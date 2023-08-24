package tm.payhas.crm.domain.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class LanguageManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    private static LanguageManager languageManager;

    public static String LANG_TK = "tk";
    public static String LANG_RU = "ru";

    private static final String KEY_LANGUAGE = "key_language";

    // Sharedpref file name
    private static final String PREF_NAME = "LANGUAGE";


    public static LanguageManager newInstance(Context context) {

        if (languageManager == null) {
            languageManager = new LanguageManager(context);
        }
        return languageManager;
    }


    // Constructor
    private LanguageManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLanguage(String language) {
        editor.putString(KEY_LANGUAGE, language);
        editor.commit();
    }


    public String getLanguage() {
        return pref.getString(KEY_LANGUAGE, LANG_RU);
    }

    /**
     * Clear session details
     */
    @SuppressLint("NewApi")
    public void clear() {
        editor.clear();
        editor.commit();
    }


}