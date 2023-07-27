package tm.payhas.crm.activity;

import static tm.payhas.crm.helpers.LanguageManager.LANG_TK;
import static tm.payhas.crm.helpers.StaticMethods.transparentStatusAndNavigation;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import tm.payhas.crm.helpers.LanguageManager;
import tm.payhas.crm.preference.AccountPreferences;

public class ActivitySplashScreen extends AppCompatActivity {
    private AccountPreferences ac;
    private LanguageManager languageManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ac = new AccountPreferences(this);
        transparentStatusAndNavigation(this);
        languageManager = LanguageManager.newInstance(this);
        if (languageManager.getLanguage().equals(LANG_TK)) {
            setLocale("tk");
            languageManager.setLanguage("tk");
        } else {
            setLocale("ru");
            languageManager.setLanguage("ru");
        }

        new Handler().postDelayed(() -> {
            if (ac.getLoggedIn()) {
                Intent intent;
                if (!ac.getPassword().equals("")) {
                    intent = new Intent(this, ActivityPassword.class);
                } else {
                    intent = new Intent(this, ActivityMain.class);
                }
                startActivity(intent);
            } else {
                Intent intent = new Intent(ActivitySplashScreen.this, ActivityLoginRegister.class);
                startActivity(intent);
            }
            finish();
        }, 100);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
