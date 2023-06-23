package tm.payhas.crm.activity;

import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.hideAdd;
import static tm.payhas.crm.helpers.Common.menuBar;
import static tm.payhas.crm.helpers.StaticMethods.initSystemUIViewListeners;
import static tm.payhas.crm.helpers.StaticMethods.transparentStatusAndNavigation;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import tm.payhas.crm.R;
import tm.payhas.crm.fragment.FragmentCloudFile;
import tm.payhas.crm.fragment.FragmentCloudFolder;
import tm.payhas.crm.fragment.FragmentFlow;
import tm.payhas.crm.fragment.FragmentHome;
import tm.payhas.crm.fragment.FragmentMessages;
import tm.payhas.crm.helpers.SoftInputAssist;
import tm.payhas.crm.interfaces.DataFileSelectedListener;
import tm.payhas.crm.preference.AccountPreferences;
import tm.payhas.crm.webSocket.WebSocket;

public class ActivityMain extends AppCompatActivity {

    public static FragmentManager mainFragmentManager;
    private FrameLayout root;
    private SoftInputAssist softInputAssist;
    public static WebSocket webSocket;
    private AccountPreferences ac;

    @Override
    protected void onStart() {
        super.onStart();
        webSocket.createWebSocketClient();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webSocket = new WebSocket(getApplicationContext(), this);
        ac = new AccountPreferences(this);
        root = findViewById(R.id.main_content);
        transparentStatusAndNavigation(this);
        ActivityLoginRegister.mainFragmentManager = getSupportFragmentManager();
        mainFragmentManager = getSupportFragmentManager();
        softInputAssist = new SoftInputAssist(this);
        setContent();

    }


    private void setContent() {
        if (mainFragmentManager.findFragmentByTag(FragmentFlow.class.getSimpleName()) == null)
            addFragment(mainFragmentManager, R.id.main_content, FragmentFlow.newInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSystemUIViewListeners(root);
        softInputAssist.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        softInputAssist.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        softInputAssist.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Fragment home = mainFragmentManager.findFragmentByTag(FragmentHome.class.getSimpleName());
        Fragment cloudFolder = mainFragmentManager.findFragmentByTag(FragmentCloudFolder.class.getSimpleName());
        Fragment cloudFile = mainFragmentManager.findFragmentByTag(FragmentCloudFile.class.getSimpleName());
        Fragment messages = mainFragmentManager.findFragmentByTag(FragmentMessages.class.getSimpleName());

        if (mainFragmentManager.getBackStackEntryCount() == 1) {
            assert cloudFolder != null;
            if (cloudFolder.isVisible()) {
                if (ac.getCloudSelectable()) {
                    if (cloudFolder instanceof DataFileSelectedListener) {
                        ((DataFileSelectedListener) cloudFolder).setUnSelectable();
                    }
                    return;
                }
            }
            assert cloudFile != null;
            if (cloudFile.isVisible()) {
                if (ac.getFileSelectable()) {
                    if (cloudFile instanceof DataFileSelectedListener) {
                        ((DataFileSelectedListener) cloudFile).setUnSelectable();
                    }

                }
                return;


            }
            if (home != null && home.isVisible()) {
                finish();
            } else {
                if (home == null) {
                    assert messages != null;
                    hideAdd(messages, messages.getClass().getSimpleName(), mainFragmentManager, R.id.secondary_content);
                    menuBar.setSelectedItemId(R.id.chat);
                } else {
                    hideAdd(home, home.getClass().getSimpleName(), mainFragmentManager, R.id.secondary_content);
                    menuBar.setSelectedItemId(R.id.home);
                }
            }
        } else super.onBackPressed();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}