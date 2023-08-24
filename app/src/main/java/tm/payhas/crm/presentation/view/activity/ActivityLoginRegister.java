package tm.payhas.crm.presentation.view.activity;

import static tm.payhas.crm.domain.helpers.StaticMethods.transparentStatusAndNavigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import tm.payhas.crm.R;
import tm.payhas.crm.presentation.view.fragment.FragmentLogin;
import tm.payhas.crm.domain.helpers.Common;

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

    @Override
    public void onBackPressed() {
        Fragment login = mainFragmentManager.findFragmentByTag(FragmentLogin.class.getSimpleName());
        if (mainFragmentManager.getBackStackEntryCount() == 1 && login != null && login.isVisible()) {
            finish();
        } else
            super.onBackPressed();
    }
}