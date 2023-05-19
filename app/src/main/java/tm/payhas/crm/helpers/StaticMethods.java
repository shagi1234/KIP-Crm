package tm.payhas.crm.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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

import java.util.Objects;

import tm.payhas.crm.R;
import tm.payhas.crm.preference.AccountPreferences;

public class StaticMethods {

    public static int statusBarHeight;
    public static int navigationBarHeight;
    public static Toast generalToast;

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

}
