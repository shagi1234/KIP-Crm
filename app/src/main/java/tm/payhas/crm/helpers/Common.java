package tm.payhas.crm.helpers;

import static android.view.Gravity.BOTTOM;
import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.adapters.AdapterChatContact.PRIVATE;
import static tm.payhas.crm.fragment.FragmentChatRoom.CAMERA_REQUEST;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tm.payhas.crm.R;
import tm.payhas.crm.api.response.RetrofitClient;
import tm.payhas.crm.api.service.Services;
import tm.payhas.crm.fragment.FragmentOpenGallery;

public class Common {
    public static BottomNavigationView menuBar;

    public static void addFragment(FragmentManager fm, int containerId, Fragment fragment) {
        String backStackName = fragment.getClass().getSimpleName();

        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_anim, 0, 0, R.anim.pop_exit_anim);
        ft.add(containerId, fragment, backStackName);
        ft.addToBackStack(backStackName);

        ft.commit();
    }

    public static Services getApi() {
        return (Services) RetrofitClient.createRequest(Services.class);
    }


    public static void hideAdd(Fragment fragment, String tagFragmentName, FragmentManager mFragmentManager, int frame) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }


        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);

        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(frame, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commit();
    }

    public static void removeShow(Fragment fragment, String tagFragmentName, FragmentManager mFragmentManager, int frame) {

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {


            fragmentTemp = fragment;

            fragmentTransaction.add(frame, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);

        }
        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();

    }

    public static void replaceFragment(FragmentManager fm, int containerId, Fragment fragment) {
        String backStackName = fragment.getClass().getName();

        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_anim, 0, 0, R.anim.pop_exit_anim);
        ft.replace(containerId, fragment, backStackName);

        ft.addToBackStack(backStackName);

        ft.commit();

    }

    public static String normalTime(String dateGiven) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = format.parse(dateGiven);
            String stringDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
            String stringTime = new SimpleDateFormat("HH:mm").format(date);
            return stringTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String normalDate(String dateGiven) {
        String initialStringDate = dateGiven;
        Locale us = new Locale("US");
        String dateTaken = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", us);
        try {
            Date date = format.parse(initialStringDate);
            String stringDate = new SimpleDateFormat("yyyy/MM/dd", us).format(date);
            String stringTime = new SimpleDateFormat("HH:mm", us).format(date);
            String finalDateTime = stringDate.concat(" ").concat(stringTime);
            dateTaken = stringDate;
            Log.i("Date_and_Time", "" + finalDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTaken;
    }

    public static void showDialog(Activity activity, Context context, int roomId, View view) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_behavoior);

        FrameLayout cameraClicker = dialog.findViewById(R.id.clicker_camera);
        FrameLayout galleryClicker = dialog.findViewById(R.id.clicker_gallery);
        FrameLayout clickerFile = dialog.findViewById(R.id.clicker_file);
        cameraClicker.setOnClickListener(view1 -> {
            view1.setVisibility(View.VISIBLE);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
            dialog.dismiss();
        });
        galleryClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedMedia.getArrayList().clear();
                addFragment(mainFragmentManager, R.id.main_content, FragmentOpenGallery.newInstance(1, roomId, FragmentOpenGallery.SINGLE, PRIVATE));
                dialog.dismiss();
            }
        });
        clickerFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(BOTTOM);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Uri uri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


}