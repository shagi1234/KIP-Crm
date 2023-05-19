package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.helpers.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentProfileBinding;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding b;


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
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.parent,
                0,
                50,
                0,
                0), 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentProfileBinding.inflate(inflater);
        setBackground();
        return b.getRoot();
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.ntfImage, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.langImg, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.pinImage, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.bgProfileDetails, R.color.profile_icon_bg, 0, 8, false, 0);
        setBackgroundDrawable(getContext(), b.layLogout, R.color.profile_icon_bg, 0, 8, false, 0);
    }
}