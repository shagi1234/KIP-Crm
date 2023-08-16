package tm.payhas.crm.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationUtils {

    public static void showNotification(Context context, String title, String message, int messageId) {
        Intent serviceIntent = new Intent(context, MyForegroundService.class);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("message", message);
        serviceIntent.putExtra("messageId", messageId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}
