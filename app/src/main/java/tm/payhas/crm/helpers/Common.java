package tm.payhas.crm.helpers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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


}