package tm.payhas.crm.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import tm.payhas.crm.R;


public abstract class DownloadAnyFile extends AsyncTask<String, Void, Boolean> {

    private int DOWNLOAD_NOTIFICATION_ID;

    public abstract void onSuccess(String local_file_uri);

    public abstract void error();

    public abstract void downloading(int progress);

    private final Activity activity;
    private final Context context;
    private String localFileUri;
    private final String fileName;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManagerCompat;

    String file_uri;


    public DownloadAnyFile(Activity activity, Context context, String file_uri, String fileName, int DOWNLOAD_NOTIFICATION_ID) {
        this.activity = activity;
        this.DOWNLOAD_NOTIFICATION_ID = DOWNLOAD_NOTIFICATION_ID;
        this.context = context;
        this.file_uri = file_uri;
        this.fileName = fileName;

    }

    private NotificationCompat.Builder createNotificationBuilder(String channelId) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = activity.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        return new NotificationCompat.Builder(context, channelId);
    }


    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            onSuccess(file_uri + "/" + fileName);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    @SuppressLint("MissingPermission")
    @Override
    protected Boolean doInBackground(final String... args) {
        int count;

        File file = new File(file_uri, fileName);

        notificationManagerCompat = NotificationManagerCompat.from(context);

        Intent notifyIntent = new Intent();
        PendingIntent notifyPendingIntent;

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            notifyPendingIntent = PendingIntent.getActivity(context, DOWNLOAD_NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            notifyPendingIntent = PendingIntent.getActivity(context, DOWNLOAD_NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        notificationBuilder = createNotificationBuilder("downloader_channel");
        try {
            notificationBuilder.setContentIntent(notifyPendingIntent);
            notificationBuilder.setTicker("Start downloading from the server");
            notificationBuilder.setOngoing(false);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
            notificationBuilder.setContentTitle(fileName);
            notificationBuilder.setContentText("0%");
            notificationBuilder.setProgress(100, 0, false);
            notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());


            URL url = new URL(args[0]);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false); // Don't look at possibly cached data


            connection.connect();

            int lenghtOfFile = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            OutputStream output = new FileOutputStream(file);

            byte[] data = new byte[4096];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                int percentage = (int) ((total * 100) / lenghtOfFile);

                try {
                    downloading(percentage);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Download_audio", "downloading: " + e.getMessage());
                }
                notificationBuilder.setContentText(percentage + "%");
                notificationBuilder.setProgress(100, percentage, false);
                notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                Log.e("Download_audio", "downloading: " + (int) ((total * 100) / lenghtOfFile));

                if (percentage == 100) {
                    notificationBuilder.setContentTitle("Downloaded");
                    notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                    notificationBuilder.setOngoing(false);
                    notificationBuilder.setAutoCancel(true);
                    notificationBuilder.setContentText("Done");
                    notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
                }

                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            return true;


        } catch (Exception e) {
            notificationBuilder.setContentTitle("Download failed");
            notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
            notificationBuilder.setOngoing(false);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setContentText("Fail");
            notificationBuilder.setProgress(0, 0, false);
            notificationManagerCompat.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
            file.delete();
            error();
            return false;
        }
    }
}
