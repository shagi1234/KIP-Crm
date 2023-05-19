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
    private static final String  IS_LOGGED_IN = "false";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_SURNAME = "_surname";
    private static final String PREF_BIRTHDAY = "_birthday";
    private static final String PREF_USER_NAME = "_username";
    private static final String PREF_LANGUAGE = "_language";
    private static final String PREF_LASTNAME = "_lastname";
    private static final String PREF_BIRTH_PLACE = "_birthplace";
    private static final String PREF_AVATAR_URL = "_avatar";


    public static AccountPreferences newInstance(Context context) {
        if (accountPreferences == null) {
            accountPreferences = new AccountPreferences(context);
        }
        return accountPreferences;
    }

    public void setIsLoggedIn(String isLoggedIn) {
        editor.putString(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public String  getLoggedIn() {
        if (_context == null) return "false";
        else
            return pref.getString(IS_LOGGED_IN, "false");
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
            return "Bearer " +pref.getString(PREF_TOKEN, "");
        }
    }

    public String getTokenForWebSocket() {
        if (_context == null) {
            return "";
        } else {
            return pref.getString(PREF_TOKEN, "");
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

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
