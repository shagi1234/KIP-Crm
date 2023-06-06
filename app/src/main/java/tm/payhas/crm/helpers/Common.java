package tm.payhas.crm.helpers;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tm.payhas.crm.R;
import tm.payhas.crm.api.response.RetrofitClient;
import tm.payhas.crm.api.service.Services;

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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss'Z'", us);
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


}