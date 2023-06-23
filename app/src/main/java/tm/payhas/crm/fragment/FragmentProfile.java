package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentProfileBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentProfile extends Fragment {
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
        if (getArguments() != null) {
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
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
        return b.getRoot();
    }

    private void setInfo() {
        b.name.setText(accountPreferences.getUserName() + "  " + accountPreferences.getPrefSurname());
        b.phoneNumber.setText(accountPreferences.getPhoneNumber());
    }

    private void initListeners() {
        b.profileDetails.setOnClickListener(view -> {
            b.profileDetails.setEnabled(false);
            Common.addFragment(mainFragmentManager, R.id.main_content, FragmentUserInfo.newInstance(accountPreferences.getAuthorId()));
            new Handler().postDelayed(() -> b.profileDetails.setEnabled(true), 200);
        });
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.ntfImage, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.langImg, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.pinImage, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.bgProfileDetails, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.layLogout, R.color.profile_icon_bg, 0, 8, false, 0);
    }
}