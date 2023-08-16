package tm.payhas.crm.activity;

import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.hideAdd;
import static tm.payhas.crm.helpers.Common.menuBar;
import static tm.payhas.crm.helpers.StaticMethods.initSystemUIViewListeners;
import static tm.payhas.crm.helpers.StaticMethods.transparentStatusAndNavigation;
import static tm.payhas.crm.statics.StaticConstants.MEDIA_PLAYER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.api.request.RequestFcmToken;
import tm.payhas.crm.api.response.ResponseFcmToken;
import tm.payhas.crm.fragment.FragmentChangePassword;
import tm.payhas.crm.fragment.FragmentCloudFile;
import tm.payhas.crm.fragment.FragmentCloudFolder;
import tm.payhas.crm.fragment.FragmentContacts;
import tm.payhas.crm.fragment.FragmentFlow;
import tm.payhas.crm.fragment.FragmentHome;
import tm.payhas.crm.fragment.FragmentMessages;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.MyForegroundService;
import tm.payhas.crm.helpers.SoftInputAssist;
import tm.payhas.crm.interfaces.DataFileSelectedListener;
import tm.payhas.crm.interfaces.OnRefresh;
import tm.payhas.crm.preference.AccountPreferences;
import tm.payhas.crm.preference.FcmPreferences;
import tm.payhas.crm.webSocket.WebSocket;

public class ActivityMain extends AppCompatActivity {

    public static FragmentManager mainFragmentManager;
    private FrameLayout root;
    private SoftInputAssist softInputAssist;
    public static WebSocket webSocket;
    private AccountPreferences ac;
    private FcmPreferences fcmPreferences;

    @Override
    protected void onStart() {
        super.onStart();
        webSocket.createWebSocketClient();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        startService(serviceIntent);
        webSocket = new WebSocket(getApplicationContext(), this);
        ac = new AccountPreferences(this);
        root = findViewById(R.id.main_content);
        transparentStatusAndNavigation(this);
        ActivityLoginRegister.mainFragmentManager = getSupportFragmentManager();
        mainFragmentManager = getSupportFragmentManager();
        softInputAssist = new SoftInputAssist(this);
        fcmPreferences = FcmPreferences.newInstance(this);
        setFcmToken();
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
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onBackPressed() {
        Fragment home = mainFragmentManager.findFragmentByTag(FragmentHome.class.getSimpleName());
        Fragment changePassword = mainFragmentManager.findFragmentByTag(FragmentChangePassword.class.getSimpleName());
        Fragment cloudFolder = mainFragmentManager.findFragmentByTag(FragmentCloudFolder.class.getSimpleName());
        Fragment cloudFile = mainFragmentManager.findFragmentByTag(FragmentCloudFile.class.getSimpleName());
        Fragment messages = mainFragmentManager.findFragmentByTag(FragmentMessages.class.getSimpleName());

        if (MEDIA_PLAYER != null && MEDIA_PLAYER.isPlaying()) {
            MEDIA_PLAYER.stop();
            MEDIA_PLAYER.release();
            MEDIA_PLAYER = null;
        }

        if (mainFragmentManager.getBackStackEntryCount() == 1) {

            if (cloudFolder != null && cloudFolder.isVisible()) {
                if (ac.getCloudSelectable()) {
                    if (cloudFolder instanceof DataFileSelectedListener) {
                        ((DataFileSelectedListener) cloudFolder).setUnSelectable();
                    }
                    return;
                }
            }
            if (cloudFile != null && cloudFile.isVisible()) {
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

    private void setFcmToken() {
        if (!fcmPreferences.getIsSent()) {
            RequestFcmToken requestFcmToken = new RequestFcmToken();
            requestFcmToken.setFcmtoken(fcmPreferences.getFcm());
            Common.getApi().setFcmToken(ac.getToken(), requestFcmToken).enqueue(new Callback<ResponseFcmToken>() {
                @Override
                public void onResponse(Call<ResponseFcmToken> call, Response<ResponseFcmToken> response) {
                    if (response.isSuccessful()) {
                        Log.e("FCM_TOKEN", "onResponse: " + "Succes");
                        fcmPreferences.setIsSent(true);
                    }
                }

                @Override
                public void onFailure(Call<ResponseFcmToken> call, Throwable t) {
                    Log.e("FCM_TOKEN", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}