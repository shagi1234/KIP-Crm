package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.domain.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.domain.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.domain.helpers.StaticMethods.statusBarHeight;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.dao.DaoGroup;
import tm.payhas.crm.data.localdb.dao.DaoMessage;
import tm.payhas.crm.data.localdb.dao.DaoUser;
import tm.payhas.crm.data.localdb.preference.FcmPreferences;
import tm.payhas.crm.data.localdb.preference.NotificationPreferences;
import tm.payhas.crm.data.localdb.room.MessagesDatabase;
import tm.payhas.crm.databinding.FragmentProfileBinding;
import tm.payhas.crm.domain.helpers.NotificationHelper;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.activity.ActivitySplashScreen;
import tm.payhas.crm.domain.interfaces.PasswordInterface;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class FragmentProfile extends Fragment implements PasswordInterface {
    private FragmentProfileBinding b;
    private AccountPreferences accountPreferences;


    public static FragmentProfile newInstance() {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.customTitleBar, 0, statusBarHeight, 0, 0), 50);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentProfileBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        accountPreferences = new AccountPreferences(getContext());
        setInfo();
        setBackground();
        initListeners();
        setSwitch();
        return b.getRoot();
    }

    private void setSwitch() {
        b.passwordSwitcher.setChecked(!accountPreferences.getPassword().equals(""));
    }

    private void setInfo() {
        b.name.setText(accountPreferences.getUserName() + "  " + accountPreferences.getPrefSurname());
        b.phoneNumber.setText(accountPreferences.getPhoneNumber());
    }

    private void initListeners() {

        b.notificationSwitcher.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (NotificationHelper.areNotificationsEnabled(getContext())) {
                NotificationPreferences.setNotificationEnabled(getContext(), isChecked);
            } else {
                NotificationHelper.requestNotificationPermission(getActivity());
            }
        });
        b.logout.setOnClickListener(view -> {
            b.logout.setEnabled(false);
            accountPreferences.getEditor().clear().commit();
            // Initialize your database instance
            MessagesDatabase database = MessagesDatabase.getInstance(getContext());
            // Clear all data from your database tables in a background thread
            clearAllDataInBackground(database);
            FcmPreferences.newInstance(getContext()).setIsSent(false);
            getActivity().finish();
            startActivity(new Intent(getContext(), ActivitySplashScreen.class));
            new Handler().postDelayed(() -> b.logout.setEnabled(true), 200);
        });

        b.profileDetails.setOnClickListener(view -> {
            b.profileDetails.setEnabled(false);
            addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentUserInfo.newInstance(accountPreferences.getAuthorId()));
            new Handler().postDelayed(() -> b.profileDetails.setEnabled(true), 200);
        });
        b.layLanguage.setOnClickListener(view -> {
            b.layLanguage.setEnabled(false);
            addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(FragmentSpinner.LANGUAGE, 0, 0, null));
            new Handler().postDelayed(() -> b.layLanguage.setEnabled(true), 200);
        });
        b.layPin.setOnClickListener(view -> addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentChangePassword.newInstance(true, false)));
        b.passwordSwitcher.setOnClickListener(view -> addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentChangePassword.newInstance(true, true)));
    }

    private void clearAllDataInBackground(MessagesDatabase database) {
        new Thread(() -> {
            database.runInTransaction(() -> {
                database.messageDao().deleteAll();
                database.userDao().deleteAll();
                database.groupDao().deleteAll();
            });
        }).start();
    }

    private void setBackground() {
        b.notificationSwitcher.setChecked(NotificationPreferences.isNotificationEnabled(getContext()));
        setBackgroundDrawable(getContext(), b.ntfImage, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.langImg, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.pinImage, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.bgProfileDetails, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.layLogout, R.color.profile_icon_bg, 0, 8, false, 0);
    }

    @Override
    public void setEnabled() {
        b.passwordSwitcher.setChecked(!accountPreferences.getPassword().equals(""));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NotificationHelper.REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                b.notificationSwitcher.setChecked(true); // Enable the switch
                NotificationPreferences.setNotificationEnabled(getContext(), true);
            } else {
                // Permission denied
                b.notificationSwitcher.setChecked(false); // Disable the switch
                NotificationPreferences.setNotificationEnabled(getContext(), false);
            }
        }
    }

}