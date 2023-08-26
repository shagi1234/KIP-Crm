package tm.payhas.crm.domain.helpers;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ProgressBar;

public class DownloadReceiver extends BroadcastReceiver {

    private ProgressBar progressBar;
    private DownloadProgressListener progressListener;

    public DownloadReceiver(ProgressBar progressBar, DownloadProgressListener progressListener) {
        this.progressBar = progressBar;
        this.progressListener = progressListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        long downloadId = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            @SuppressLint("Range") int downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            @SuppressLint("Range") int total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                // Download completed successfully
                progressBar.setProgress(100);

                // Perform actions after download completion, e.g., open the downloaded file
                // You can use an Intent to open the file, or show a notification, etc.
                // For example:
                openDownloadedFile(context);
            } else if (status == DownloadManager.STATUS_FAILED) {
                // Handle download failure
            } else {
                progressBar.setProgress((int) ((downloaded * 100L) / total));
            }

            updateProgress((int) ((downloaded * 100L) / total)); // Notify progress listener
        }

        cursor.close();
    }

    private void updateProgress(int progress) {
        if (progressListener != null) {
            progressListener.onProgressUpdate(progress);
        }
    }

    private void openDownloadedFile(Context context) {
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle case where there is no app to handle the intent
            e.printStackTrace();
        }
    }
}
