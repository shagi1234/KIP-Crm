package tm.payhas.crm.presentation.view.activity;

import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.Common.hideAdd;
import static tm.payhas.crm.domain.helpers.Common.menuBar;
import static tm.payhas.crm.domain.helpers.StaticMethods.initSystemUIViewListeners;
import static tm.payhas.crm.domain.helpers.StaticMethods.transparentStatusAndNavigation;
import static tm.payhas.crm.domain.statics.StaticConstants.MEDIA_PLAYER;
import static tm.payhas.crm.presentation.view.adapters.AdapterChatContact.GROUP;
import static tm.payhas.crm.presentation.view.adapters.AdapterChatContact.PRIVATE;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.preference.FcmPreferences;
import tm.payhas.crm.data.localdb.preference.NotificationPreferences;
import tm.payhas.crm.data.remote.api.request.RequestFcmToken;
import tm.payhas.crm.data.remote.api.response.ResponseFcmToken;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.presentation.view.fragment.FragmentChangePassword;
import tm.payhas.crm.presentation.view.fragment.FragmentChatRoom;
import tm.payhas.crm.presentation.view.fragment.FragmentCloudFile;
import tm.payhas.crm.presentation.view.fragment.FragmentCloudFolder;
import tm.payhas.crm.presentation.view.fragment.FragmentFlow;
import tm.payhas.crm.presentation.view.fragment.FragmentMessages;
import tm.payhas.crm.domain.helpers.SoftInputAssist;
import tm.payhas.crm.domain.interfaces.DataFileSelectedListener;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;
import tm.payhas.crm.data.remote.webSocket.WebSocket;

public class ActivityMain extends AppCompatActivity {

    public static FragmentManager mainFragmentManager;
    private FrameLayout root;
    private SoftInputAssist softInputAssist;
    public static WebSocket webSocket;
    private AccountPreferences ac;
    private FcmPreferences fcm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webSocket = new WebSocket(this);
        ac = new AccountPreferences(this);
        root = findViewById(R.id.main_content);
        transparentStatusAndNavigation(this);
        ActivityLoginRegister.mainFragmentManager = getSupportFragmentManager();
        mainFragmentManager = getSupportFragmentManager();
        softInputAssist = new SoftInputAssist(this);
        fcm = new FcmPreferences(this);
        Log.e("FCM_TOKEN", "onCreate: " + fcm.getIsSent());
        setFcmToken();
        setContent();
        NotificationPreferences.setLaunched(this, true);
        dismissNotification();
        Log.e("Activity main", "onCreate: " + ac.getToken());
    }

    private void dismissNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private void setFcmToken() {
        if (!fcm.getIsSent()) {
            RequestFcmToken requestFcmToken = new RequestFcmToken();
            requestFcmToken.setFcmtoken(fcm.getFcm());
            Common.getApi().setFcmToken(ac.getToken(), requestFcmToken).enqueue(new Callback<ResponseFcmToken>() {
                @Override
                public void onResponse(Call<ResponseFcmToken> call, Response<ResponseFcmToken> response) {
                    if (response.isSuccessful()) {
                        Log.e("FCM_TOKEN", "onResponse: " + "Succes");
                        fcm.setIsSent(true);
                    }
                }

                @Override
                public void onFailure(Call<ResponseFcmToken> call, Throwable t) {
                    Log.e("FCM_TOKEN", "onFailure: " + t.getMessage());
                }
            });
        }
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
        dismissNotification();
    }

    @Override
    protected void onPause() {
        super.onPause();
        softInputAssist.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int roomId = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("roomId")));
        String type = intent.getStringExtra("roomType");
        int userId = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("authorId")));
        if (Objects.equals(type, "private")) {
            addFragment(mainFragmentManager, R.id.main_content, FragmentChatRoom.newInstance(roomId, userId, PRIVATE));
        } else {
            addFragment(mainFragmentManager, R.id.main_content, FragmentChatRoom.newInstance(roomId, userId, GROUP));
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissNotification();
        softInputAssist.onDestroy();
        NotificationPreferences.setLaunched(this, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!webSocket.connected) {
            webSocket.createWebSocketClient();
        }

    }

    @Override
    public void onBackPressed() {
        Fragment home = mainFragmentManager.findFragmentByTag(FragmentMessages.class.getSimpleName());
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


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}