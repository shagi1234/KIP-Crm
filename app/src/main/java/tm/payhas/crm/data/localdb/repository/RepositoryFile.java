package tm.payhas.crm.data.localdb.repository;

import static tm.payhas.crm.domain.helpers.FileUtil.copyFileStream;
import static tm.payhas.crm.domain.statics.StaticConstants.APPLICATION_DIR_NAME;
import static tm.payhas.crm.domain.statics.StaticConstants.FILES_DIR;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;

import java.io.File;

import tm.payhas.crm.domain.helpers.FileUtil;

public class RepositoryFile {

    private Context context;

    public RepositoryFile(Context context) {
        this.context = context;
    }

    public File processFile(Uri uri) {
        File resultFile = null;

        try {
            String filename;
            String mimeType = context.getContentResolver().getType(uri);

            if (mimeType == null) {
                String path = FileUtil.getPath(context, uri);

                if (path == null) {
                    filename = FilenameUtils.getName(uri.toString());
                } else {
                    File file = new File(path);
                    filename = file.getName();
                }
            } else {
                Uri returnUri = uri;

                Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }

            String sourcePath = Environment.getExternalStorageDirectory() + File.separator + APPLICATION_DIR_NAME + FILES_DIR;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                resultFile = new File(context.getExternalFilesDir("Salam/Files/"), filename);
                copyFileStream(resultFile, uri, context);
            } else {
                resultFile = new File(sourcePath + filename);
                copyFileStream(resultFile, uri, context);
            }
        } catch (Exception e) {
            Log.e("error", "processFile: " + e.getMessage());
        }

        return resultFile;
    }
}
