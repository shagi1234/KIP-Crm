package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.helpers.StaticMethods.statusBarHeight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import tm.payhas.crm.R;
import tm.payhas.crm.activity.ActivitySplashScreen;
import tm.payhas.crm.databinding.FragmentProfileBinding;
import tm.payhas.crm.interfaces.PasswordInterface;
import tm.payhas.crm.preference.AccountPreferences;

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
        new Handler().postDelayed(() -> setPadding(b.customTitleBar,
                0,
                statusBarHeight,
                0,
                0), 50);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        b.logout.setOnClickListener(view -> {
            b.logout.setEnabled(false);
            accountPreferences.getEditor().clear().commit();
            getActivity().finish();
            startActivity(new Intent(getContext(), ActivitySplashScreen.class));
            new Handler().postDelayed(() -> b.logout.setEnabled(true), 200);
        });
        b.profileDetails.setOnClickListener(view -> {
            b.profileDetails.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentUserInfo.newInstance(accountPreferences.getAuthorId()));
            new Handler().postDelayed(() -> b.profileDetails.setEnabled(true), 200);
        });
        b.layLanguage.setOnClickListener(view -> {
            b.layLanguage.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(FragmentSpinner.LANGUAGE, 0, 0, null));
            new Handler().postDelayed(() -> b.layLanguage.setEnabled(true), 200);
        });
        b.layPin.setOnClickListener(view -> addFragment(mainFragmentManager, R.id.main_content, FragmentChangePassword.newInstance(true, false)));
        b.passwordSwitcher.setOnClickListener(view -> addFragment(mainFragmentManager, R.id.main_content, FragmentChangePassword.newInstance(true, true)));
    }

    private void setBackground() {
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
}