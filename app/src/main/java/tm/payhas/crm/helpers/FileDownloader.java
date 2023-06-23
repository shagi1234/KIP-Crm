package tm.payhas.crm.helpers;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class FileDownloader {

    public static void downloadFile(Context context, String fileUrl, String fileName) {
        String mimeType = null;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));

        // Set the download destination directory and file name
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        // Extract the file extension from the file name
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        // Map file extension to MIME type

        if (fileExtension.equalsIgnoreCase("pdf")) {
            mimeType = "application/pdf";
        } else if (fileExtension.equalsIgnoreCase("pptx")) {
            mimeType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        }
        request.setMimeType(mimeType);
        // Add more file types as needed

        // Get the download service and enqueue the download request
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);
    }

}

