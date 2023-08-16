package tm.payhas.crm.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import tm.payhas.crm.R;
import tm.payhas.crm.activity.ActivityMain;

public class MyForegroundService extends Service {

    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        int messageId = intent.getIntExtra("messageId", 0);

        createNotificationChannel();

        // Create an Intent to open MainActivity
        Intent mainActivityIntent = new Intent(this, ActivityMain.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.ic_logo_lemmer).setContentIntent(pendingIntent) // Set the PendingIntent to open MainActivity
                .build();

        startForeground(messageId, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();// Stop the foreground service and stop the service itself
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
