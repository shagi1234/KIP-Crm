package tm.payhas.crm.fcm;


import static android.os.Build.USER;
import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;

import tm.payhas.crm.R;
import tm.payhas.crm.activity.ActivityMain;
import tm.payhas.crm.fragment.FragmentFlow;
import tm.payhas.crm.preference.AccountPreferences;
import tm.payhas.crm.preference.FcmPreferences;


public class FirebasePushMessage extends FirebaseMessagingService {

    private static final String TAG = "FirebaseService";
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Log.e(TAG, "handleIntent: " + "\n" +
                " title " + intent.getExtras().getString("title") + "\n" +
                " body " + intent.getExtras().getString("body") + "\n" +
                " roomId " + intent.getExtras().getString("roomId") + "\n" +
                " to " + intent.getExtras().getString("to") + "\n" +
                " sound " + intent.getExtras().getString("sound")
        );
        if (intent.getExtras().getString("title") != null) {
            getFirebaseMessage(intent.getExtras().getString("title"), intent.getExtras().getString("body"), intent.getExtras().getString("roomId"));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FcmPreferences.newInstance(getApplicationContext()).setFcm(token);
        Log.e(TAG, "onNewToken: " + token);
    }

    private void getFirebaseMessage(String title, String body, String taskId) {

        Intent activityIntent = new Intent(getApplicationContext(), ActivityMain.class);

        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        activityIntent.putExtra("uuid", taskId);
        activityIntent.putExtra("to", USER);

        int[] a = AccountPreferences.newInstance(getApplicationContext()).getNotificationIds();

        int id = Integer.parseInt(taskId);

        if (a == null) {

            a = new int[1];
            a[0] = id;
            AccountPreferences.newInstance(getApplicationContext()).saveIds(a);
        } else {
            if (!contains(a, id)) {
                int[] aNew = new int[a.length + 1];
                System.arraycopy(a, 0, aNew, 0, a.length);
                aNew[a.length] = id;
                AccountPreferences.newInstance(getApplicationContext()).saveIds(aNew);
            }
        }


        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, activityIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        if (title == null) title = "";

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(title);

        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "guncha");
        mBuilder.setSmallIcon(R.drawable.ic_logo_lemmer)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(bigText)
                .setContentText(body)
                .setVibrate(new long[]{1500, 1000, 1500, 1000})
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "guncha";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Show necessary orders",
                    NotificationManager.IMPORTANCE_HIGH);

            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(1, mBuilder.build());

    }

    public static boolean contains(final int[] arr, final int key) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Arrays.stream(arr).anyMatch(i -> i == key);
        } else {
            boolean isContains = false;

            for (int j : arr) {
                if (j == key) {
                    isContains = true;
                    break;
                }
            }
            return isContains;
        }
    }

}