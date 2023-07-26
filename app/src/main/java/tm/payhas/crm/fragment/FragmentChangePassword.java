package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterKeyboard;
import tm.payhas.crm.databinding.FragmentChangePasswordBinding;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentChangePassword extends Fragment {

    private FragmentChangePasswordBinding b;
    private AccountPreferences accountPreferences;
    private final ArrayList<Integer> keyboardCount = new ArrayList<>();
    private final ArrayList<FrameLayout> togalajyklar = new ArrayList<>();
    private boolean change;
    private boolean isPasswordSwitch;

    public static FragmentChangePassword newInstance(boolean change, boolean isPasswordSwitch) {
        Bundle args = new Bundle();
        args.putBoolean("is_change", change);
        args.putBoolean("is_password_switch", isPasswordSwitch);
        FragmentChangePassword fragment = new FragmentChangePassword();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.constraint, 0, 50, 0, 0);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountPreferences = AccountPreferences.newInstance(getContext());
        if (getArguments() != null) {
            change = getArguments().getBoolean("is_change");
            isPasswordSwitch = getArguments().getBoolean("is_password_switch");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentChangePasswordBinding.inflate(inflater, container, false);
        addKeyboardCount();
        init();
        Log.e("Password", "onCreateView: "+accountPreferences.getPassword());
        return b.getRoot();
    }

    private void init() {
        b.otpEditText.setEnabled(false);
        if (getActivity() == null) return;
        setBackgroundDrawable(getContext(), b.first, R.color.color_transparent, R.color.primary, 0, true, 1);
        setBackgroundDrawable(getContext(), b.second, R.color.color_transparent, R.color.primary, 0, true, 1);
        setBackgroundDrawable(getContext(), b.third, R.color.color_transparent, R.color.primary, 0, true, 1);
        setBackgroundDrawable(getContext(), b.four, R.color.color_transparent, R.color.primary, 0, true, 1);

        if (accountPreferences.getPassword().equals("")) {
            b.text.setText(getActivity().getResources().getString(R.string.enter_new_code));
        } else if (change) {
            b.text.setText(getActivity().getResources().getString(R.string.enter_old_code));
        } else {
            b.text.setText(getActivity().getResources().getString(R.string.enter_code));
        }
        b.recKeyboard.setLayoutManager(new GridLayoutManager(getContext(), 3));
        b.recKeyboard.setAdapter(new AdapterKeyboard(getActivity(), getContext(), b.otpEditText, keyboardCount, togalajyklar, b.circleLay, false, this, change, b.text, isPasswordSwitch));
    }


    private void addKeyboardCount() {
        keyboardCount.clear();
        keyboardCount.add(1);
        keyboardCount.add(2);
        keyboardCount.add(3);
        keyboardCount.add(4);
        keyboardCount.add(5);
        keyboardCount.add(6);
        keyboardCount.add(7);
        keyboardCount.add(8);
        keyboardCount.add(9);
        keyboardCount.add(null);
        keyboardCount.add(0);
        keyboardCount.add(null);
        togalajyklar.clear();
        togalajyklar.add(b.first);
        togalajyklar.add(b.second);
        togalajyklar.add(b.third);
        togalajyklar.add(b.four);
    }


}