package tm.payhas.crm.data.remote.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;
import tm.payhas.crm.data.localdb.preference.FcmPreferences;
import tm.payhas.crm.presentation.view.activity.ActivityMain;

public class FirebasePushMessage extends FirebaseMessagingService {

    private static final String TAG = "FirebaseService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            // Handle the received message data here
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String roomId = remoteMessage.getData().get("roomId");
            String authorId = remoteMessage.getData().get("authorId");
            String roomType = remoteMessage.getData().get("roomType");

            if (title != null) {
                showNotification(title, body, roomId, authorId, roomType);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FcmPreferences.newInstance(getApplicationContext()).setFcm(token);
        // You can also send this token to your server if needed
    }

    private void showNotification(String title, String body, String roomId, String authorId, String roomType) {
        Intent activityIntent = new Intent(getApplicationContext(), ActivityMain.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        activityIntent.putExtra("roomId", roomId);
        activityIntent.putExtra("authorId", authorId);
        activityIntent.putExtra("roomType", roomType);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                activityIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "guncha")
                .setSmallIcon(R.drawable.ic_logo_lemmer)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(body)
                .setVibrate(new long[]{1500, 1000, 1500, 1000})
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "guncha";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Show necessary orders",
                    NotificationManager.IMPORTANCE_HIGH
            );

            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        int uniqueNotificationId = (int) System.currentTimeMillis() + new Random().nextInt(10000);

        mNotificationManager.notify(uniqueNotificationId, mBuilder.build());
    }
}
