package tm.payhas.crm.activity;

import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.StaticMethods.transparentStatusAndNavigation;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import tm.payhas.crm.R;
import tm.payhas.crm.fragment.FragmentChangePassword;

public class ActivityPassword extends AppCompatActivity {

    FrameLayout passwordContent;
    FragmentManager passwordFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        passwordFragmentManager = getSupportFragmentManager();
        passwordContent = findViewById(R.id.password_content);
        transparentStatusAndNavigation(this);
        setContent();
    }

    private void setContent() {
        addFragment(passwordFragmentManager, R.id.password_content, FragmentChangePassword.newInstance(false, false));
    }
}