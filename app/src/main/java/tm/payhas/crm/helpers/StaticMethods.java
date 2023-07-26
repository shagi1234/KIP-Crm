package tm.payhas.crm.helpers;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import tm.payhas.crm.R;
import tm.payhas.crm.preference.AccountPreferences;

public class StaticMethods {

    public static int statusBarHeight;
    public static int navigationBarHeight;
    public static Toast generalToast;

    public static void vibrator(int vibrMilliseconds, Context context) {
        if (context == null) return;
        Vibrator vibrator;
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrMilliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(vibrMilliseconds);
        }
    }

    public static void setBackgroundDrawable(Context context, View view, int color, int borderColor, int corner, boolean isOval, int border) {
        if (context == null) return;

        GradientDrawable shape = new GradientDrawable();
        if (isOval) {
            shape.setShape(GradientDrawable.OVAL);
        } else {
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(dpToPx(context, corner));
        }
        if (color != 0) {
            shape.setColor(context.getResources().getColor(color));
        } else {
            shape.setColor(context.getResources().getColor(R.color.color_transparent));
        }

        if (borderColor != 0) {
            shape.setStroke(dpToPx(border, context), context.getResources().getColor(borderColor));
        }
        view.setBackground(shape);

    }

    public static void setBackgroundDrawable(Context context, View view, int color, int borderColor, int cornerLeftTop, int cornerRightTop, int cornerLeftBottom, int cornerRightBottom, boolean isOval, int border) {
        if (context == null) return;

        GradientDrawable shape = new GradientDrawable();
        if (isOval) {
            shape.setShape(GradientDrawable.OVAL);
        } else {
            shape.setShape(GradientDrawable.RECTANGLE);

            shape.setCornerRadii(new float[]{cornerLeftTop, cornerRightTop, 0, 0, 0, 0, cornerRightBottom, cornerLeftBottom});
        }
        if (color != 0) {
            shape.setColor(context.getResources().getColor(color));
        } else {
            shape.setColor(context.getResources().getColor(R.color.color_transparent));
        }

        if (borderColor != 0) {
            shape.setStroke(dpToPx(border, context), context.getResources().getColor(borderColor));
        }
        view.setBackground(shape);

    }

    public static int dpToPx(int dp, Context context) {
        if (context == null) return 0;

        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static int pxToDp(int px, final Context context) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static float dpToPx(Context context, int dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float dpToPxFloat(Context context, float dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float pxToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void setPadding(ViewGroup v, int l, int t, int r, int b) {
        try {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                v.setPadding(l, t, r, b);
                v.requestLayout();
            }
        } catch (Exception e) {
            Log.d("error", "setMargins: " + e.getMessage());
        }
    }

    public static void setMargins(ViewGroup v, int l, int t, int r, int b) {
        try {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        } catch (Exception e) {
            Log.d("SetMargins", "setMargins: " + e.getMessage());
        }
    }

    public static void setPadding(View v, int l, int t, int r, int b) {
        try {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();

                v.setPadding(l, t, r, b);
                //p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        } catch (Exception e) {
            Log.d("error", "setMargins: " + e.getMessage());
        }

    }

    public static int getWindowHeight(Activity activity) {
        if (activity == null) return 0;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getWindowWidth(Activity activity) {
        if (activity == null) return 0;
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static void transparentStatusAndNavigation(Activity activity) {

        Window window = activity.getWindow();


        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        visibility = visibility | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        window.getDecorView().setSystemUiVisibility(visibility);
        int windowManager = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        windowManager = windowManager | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        setWindowFlag(activity, windowManager, false);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        setStatusAndNavBarIconColor(activity, true);


    }

    private static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void setStatusAndNavBarIconColor(Activity activity, boolean setLight) {
        View view = activity.getWindow().getDecorView();
        if (setLight) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                view.setSystemUiVisibility(view.getSystemUiVisibility() | ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                view.setSystemUiVisibility(view.getSystemUiVisibility() | ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        }
    }

    public static void setLightStatusBar(Activity activity) {
        // status bar texty gara renk etyar

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
//            activity.getWindow().setStatusBarColor(Color.GRAY); // optional
        }
    }

    public static void setClearLightStatusBar(Activity activity) {
//        status bar texty ak renk etyar

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
//            activity.getWindow().setStatusBarColor(Color.GREEN); // optional
        }
    }

    public static void initSystemUIViewListeners(ViewGroup rootContainer) {

        rootContainer.setOnApplyWindowInsetsListener((v, windowInsets) -> {
            WindowInsets defaultInsets = v.onApplyWindowInsets(windowInsets);
            statusBarHeight = defaultInsets.getSystemWindowInsetTop();
            navigationBarHeight = defaultInsets.getSystemWindowInsetBottom();

            return defaultInsets.replaceSystemWindowInsets(
                    0, 0, 0, 0);
        });
    }

    public static void setNavBarIconsWhite(Activity activity, Context context) {
        if (activity == null || context == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Window window = activity.getWindow();
            final int lFlags = activity.getWindow().getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else {
            activity.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.color_transparent));
        }

    }

    public static void clearLightStatusBar(Activity activity) {
//        status bar texty ak renk etyar

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
//            flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
//            activity.getWindow().getDecorView().setSystemUiVisibility(flags);

            final int lFlags = activity.getWindow().getDecorView().getSystemUiVisibility();
            activity.getWindow().getDecorView().setSystemUiVisibility(lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity == null) return;
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            if (inputMethodManager.isAcceptingText()) {
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        0
                );
            }
        } catch (Exception e) {
            Log.d("error", "hideSoftKeyboard: " + e.getMessage());
        }

    }

    public static String checkLang(Context context, String tkm, String ru) {
        if (context == null) return tkm;
        AccountPreferences lm = AccountPreferences.newInstance(context);

        if (Objects.equals(lm.getLanguage(), AccountPreferences.LANG_RU)) {
            return ru;
        }
        return tkm;
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        try {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        } catch (Exception e) {
            Log.d("error", "setMargins: " + e.getMessage());
        }

    }

    public static String getOutputFilePath(Context context) {
        File directory = context.getExternalFilesDir(null);
        String fileName = "recorded_audio.3gp"; // Replace with your desired file name and extension
        return directory.getAbsolutePath() + "/" + fileName;
    }

    public static String getCurrentTime() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        return df.format(c);
    }

    public static void showToast(Activity activity, String str) {

        if (activity == null) return;

        Context applicationContext = activity.getApplicationContext();
        if (generalToast != null) {
            generalToast.cancel();
        }

        Toast toast;

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, activity.findViewById(R.id.toast_layout_root));

        setBackgroundDrawable(activity, layout.findViewById(R.id.background), R.color.error_color, 0, 5, false, 0);
        TextView text = layout.findViewById(R.id.text);
        text.setText(str);
        toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        generalToast = toast;

    }

    public static void showToast(Activity activity, int str) {

        if (activity == null) return;

        Context applicationContext = activity.getApplicationContext();
        if (generalToast != null) {
            generalToast.cancel();
        }

        Toast toast;

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, activity.findViewById(R.id.toast_layout_root));

        setBackgroundDrawable(activity, layout.findViewById(R.id.background), R.color.error_color, 0, 5, false, 0);
        TextView text = layout.findViewById(R.id.text);
        text.setText(activity.getResources().getString(str));
        toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        generalToast = toast;

    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else if ("document".equals(type)) {
                    // Handle document files
                    contentUri = MediaStore.Files.getContentUri("external");
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
