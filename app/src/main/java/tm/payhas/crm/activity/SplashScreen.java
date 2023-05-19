package tm.payhas.crm.activity;

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
        new Handler().postDelayed(() -> {
            if (ac.getLoggedIn().equals("true")) {
                Intent intent = new Intent(SplashScreen.this, ActivityMain.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashScreen.this, ActivityLoginRegister.class);
                startActivity(intent);
                finish();
            }
        }, 100);
    }
}
