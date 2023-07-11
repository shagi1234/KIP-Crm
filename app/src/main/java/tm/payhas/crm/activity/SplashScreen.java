package tm.payhas.crm.activity;

import static tm.payhas.crm.helpers.StaticMethods.transparentStatusAndNavigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tm.payhas.crm.preference.AccountPreferences;

public class SplashScreen extends AppCompatActivity {
    private AccountPreferences ac;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ac = new AccountPreferences(this);
        transparentStatusAndNavigation(this);
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
                Intent intent = new Intent(SplashScreen.this, ActivityLoginRegister.class);
                startActivity(intent);
            }
            finish();
        }, 100);
    }
}
