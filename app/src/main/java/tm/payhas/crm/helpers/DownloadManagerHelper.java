package tm.payhas.crm.helpers;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.LongSparseArray;

public class DownloadManagerHelper {
    private static DownloadManager downloadManager;
    private static LongSparseArray<String> downloadReferences;

    public static void init(Context context) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadReferences = new LongSparseArray<>();
    }

    public static long enqueueDownload(String url, String fileName) {
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        long downloadId = downloadManager.enqueue(request);
        downloadReferences.put(downloadId, fileName);

        return downloadId;
    }

    public static int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            cursor.close();
            return status;
        }

        return DownloadManager.ERROR_UNKNOWN;
    }

    public static String getDownloadFileName(long downloadId) {
        return downloadReferences.get(downloadId);
    }
}
