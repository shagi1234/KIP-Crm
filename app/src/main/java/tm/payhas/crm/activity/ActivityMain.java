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
import tm.payhas.crm.fragment.FragmentFlow;
import tm.payhas.crm.fragment.FragmentHome;
import tm.payhas.crm.fragment.FragmentMessages;
import tm.payhas.crm.helpers.SoftInputAssist;
import tm.payhas.crm.webSocket.WebSocket;

public class ActivityMain extends AppCompatActivity {

    public static FragmentManager mainFragmentManager;
    private FrameLayout root;
    private SoftInputAssist softInputAssist;
    public static WebSocket webSocket;

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

        root = findViewById(R.id.main_content);
        transparentStatusAndNavigation(this);
        ActivityLoginRegister.mainFragmentManager = getSupportFragmentManager();
        mainFragmentManager = getSupportFragmentManager();
        softInputAssist = new SoftInputAssist(this);
        setContent();
    }

    private void setContent() {
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
        Fragment messages = mainFragmentManager.findFragmentByTag(FragmentMessages.class.getSimpleName());

        if (mainFragmentManager.getBackStackEntryCount() == 1) {
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