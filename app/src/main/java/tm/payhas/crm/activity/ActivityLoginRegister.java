package tm.payhas.crm.activity;

import static tm.payhas.crm.helpers.StaticMethods.transparentStatusAndNavigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import tm.payhas.crm.R;
import tm.payhas.crm.fragment.FragmentLogin;
import tm.payhas.crm.helpers.Common;

public class ActivityLoginRegister extends AppCompatActivity {
    public static FragmentManager mainFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        transparentStatusAndNavigation(this);
        mainFragmentManager = getSupportFragmentManager();
        ActivityMain.mainFragmentManager = getSupportFragmentManager();
        setContent();
    }

    private void setContent() {
        Common.addFragment(mainFragmentManager, R.id.login_content, FragmentLogin.newInstance());

    }
}