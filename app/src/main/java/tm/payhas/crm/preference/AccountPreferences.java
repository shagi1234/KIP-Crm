package tm.payhas.crm.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class AccountPreferences {

    private static final String PREF_COLOR_CODE = "COLOR_CODE";
    private final SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final Context _context;
    int PRIVATE_MODE = 0;
    @SuppressLint("StaticFieldLeak")
    private static AccountPreferences accountPreferences;
    private static final String PREF_NAME = "Crm_account";
    public static final String LANG_RU = "ru";
    public static final String LANG_TK = "tk";
    private static final String IS_LOGGED_IN = "_is_logged_in";
    private static final String PREF_TOKEN = "_token";
    private static final String PREF_SURNAME = "_surname";
    private static final String PREF_BIRTHDAY = "_birthday";
    private static final String PREF_USER_NAME = "_username";
    private static final String PREF_LANGUAGE = "_language";
    private static final String PREF_LASTNAME = "_lastname";
    private static final String PREF_BIRTH_PLACE = "_birthplace";
    private static final String PREF_AVATAR_URL = "_avatar";
    private static final String PREF_AUTHOR_ID = "_authorId";
    private static final String PREF_PHONE_NUMBER = "_phoneNumber";
    private static final String PREF_CURRENT_PROJECT_ID = "_currentProjectId";
    private static final String CLOUD_FOLDER_SELECTABLE = "_selectable";
    private static final String CLOUD_FILE_SELECTABLE = "__selectable";
    protected final String FINGER_LOCK = "FINGER_LOCK";
    protected final String PREF_USER_PASSWORD = "USER_PASSWORD";

    public void setFingerLock(int b) {
        saveToPreference(FINGER_LOCK, b);

    }

    protected void saveToPreference(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    protected void saveToPreference(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    protected void saveToPreference(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    protected void saveToPreference(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public int getFingerLock() {
        return getInt(FINGER_LOCK, 1);
    }

    protected int getInt(String key, int defaultValue) {
        if (_context == null) return 0;
        return pref.getInt(key, defaultValue);
    }

    protected int getInt(String key) {
        return getInt(key, 0);
    }


    public static AccountPreferences newInstance(Context context) {
        if (accountPreferences == null) {
            accountPreferences = new AccountPreferences(context);
        }
        return accountPreferences;
    }

    public void setIsLoggedIn() {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.commit();
    }

    public void setFolderSelectable(boolean isSelectable) {
        editor.putBoolean(CLOUD_FOLDER_SELECTABLE, isSelectable);
        editor.commit();
    }

    public void setFileSelectable(boolean isSelectable) {
        editor.putBoolean(CLOUD_FILE_SELECTABLE, isSelectable);
        editor.commit();
    }

    public boolean getCloudSelectable() {
        if (_context == null) return false;
        return pref.getBoolean(CLOUD_FOLDER_SELECTABLE, false);
    }

    public boolean getFileSelectable() {
        if (_context == null) return false;
        return pref.getBoolean(CLOUD_FILE_SELECTABLE, false);
    }


    public void setPrefPhoneNumber(String phoneNumber) {
        editor.putString(PREF_PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public String getPhoneNumber() {
        if (_context == null) return "";
        return pref.getString(PREF_PHONE_NUMBER, "");
    }

    public void setPrefCurrentProjectId(int projectId) {
        editor.putInt(PREF_CURRENT_PROJECT_ID, projectId);
        editor.commit();
    }

    public int getPrefCurrentProjectId() {
        if (_context == null) return 0;
        return pref.getInt(PREF_CURRENT_PROJECT_ID, 0);
    }

    public boolean getLoggedIn() {
        if (_context == null) return false;
        else
            return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setPrefAuthorId(int authorId) {
        editor.putInt(PREF_AUTHOR_ID, authorId);
        editor.commit();
    }

    public int getAuthorId() {
        if (_context == null) return 0;
        else return pref.getInt(PREF_AUTHOR_ID, 0);
    }

    public void setPrefSurname(String surname) {
        editor.putString(PREF_SURNAME, surname);
        editor.commit();
    }

    public String getPrefSurname() {
        if (_context == null) return "";
        else return pref.getString(PREF_SURNAME, "");
    }

    public void setPrefBirthday(String birthday) {
        editor.putString(PREF_BIRTHDAY, birthday);
        editor.commit();
    }

    public String getPrefBirthday() {
        if (_context == null) return "";
        else return pref.getString(PREF_BIRTHDAY, "");
    }

    public void setPrefBirthPlace(String birthPlace) {
        editor.putString(PREF_BIRTH_PLACE, "");
        editor.commit();
    }

    public String getPrefBirthPlace() {
        if (_context == null) return "";
        else return pref.getString(PREF_BIRTH_PLACE, "");
    }

    public void setPrefLastname(String lastname) {
        editor.putString(PREF_LASTNAME, "");
        editor.commit();
    }

    public String getPrefLastname() {
        if (_context == null) return "";
        else return pref.getString(PREF_LASTNAME, "");
    }

    public AccountPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setPrefUserName(String userName) {
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public String getUserName() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_USER_NAME, "");
    }


    public void setToken(String token) {
        editor.putString(PREF_TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        if (_context == null) {
            return "";
        } else {
            return "Bearer " + pref.getString(PREF_TOKEN, "");
        }
    }

    public String getTokenForWebSocket() {
        if (_context == null) {
            return "";
        } else {
            return "Bearer%20" + pref.getString(PREF_TOKEN, "");
        }
    }

    public void setLanguage(String lang) {
        editor.putString(PREF_LANGUAGE, lang);
        editor.commit();
    }

    public String getLanguage() {
        if (_context == null) {
            return "tkm";
        } else
            return pref.getString(PREF_LANGUAGE, "tkm");
    }

    public void setPrefColorCode(String registerImage) {
        editor.putString(PREF_COLOR_CODE, registerImage);
        editor.commit();
    }

    public void setPrefAvatarUrl(String avatarUrl) {
        editor.putString(PREF_AVATAR_URL, avatarUrl);
        editor.commit();
    }

    public String getPrefAvatarUrl() {
        if (_context == null) return "";
        else
            return pref.getString(PREF_AVATAR_URL, "");
    }

    public String getPrefColorCode() {
        if (_context == null) {
            return "";
        } else
            return pref.getString(PREF_COLOR_CODE, "");
    }

    public void savePassword(String pass) {
        saveToPreference(PREF_USER_PASSWORD, pass);

    }

    public String getPassword() {

        return getString(PREF_USER_PASSWORD);
    }

    protected String getString(String key, String defaultValue) {
        if (_context == null) return "";
        return pref.getString(key, defaultValue);
    }

    protected String getString(String key) {
        return getString(key, "");
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
