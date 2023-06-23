package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityLoginRegister.mainFragmentManager;
import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterViewPager;
import tm.payhas.crm.databinding.FragmentMessagesBinding;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentMessages extends Fragment {
    private FragmentMessagesBinding b;
    private AdapterViewPager adapterViewPager = new AdapterViewPager(mainFragmentManager);
    private AccountPreferences ac;


    public static FragmentMessages newInstance() {
        FragmentMessages fragment = new FragmentMessages();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentMessagesBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        setUpHelpers();
        setBackground();
        setViewPager();
        setupTabIcons();
        initListeners();
        setPage();
        return b.getRoot();
    }

    private void setUpHelpers() {
        ac = new AccountPreferences(getContext());
    }

    private void setPage() {
        Picasso.get().load(BASE_PHOTO + ac.getPrefAvatarUrl()).placeholder(R.color.primary).into(b.myAvatar);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }


    private void initListeners() {
        b.searchCancel.setOnClickListener(view -> b.searchInput.setText(""));
        b.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (b.searchInput.getText().length() > 0) {
                    b.searchCancel.setVisibility(View.VISIBLE);
                } else {
                    b.searchCancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.searchBox, R.color.color_transparent, R.color.primary, 6, false, 1);
    }

    private void setViewPager() {
        adapterViewPager.addFragment(new FragmentContacts(), getResources().getString(R.string.friends));
        adapterViewPager.addFragment(new FragmentGroups(), getResources().getString(R.string.group));
        b.tabLayout.setupWithViewPager(b.viewPager);
        b.viewPager.setAdapter(adapterViewPager);
    }

    private void setupTabIcons() {
        b.tabLayout.getTabAt(0).setIcon(R.drawable.ic_profile_selectable);
        b.tabLayout.getTabAt(1).setIcon(R.drawable.ic_group);
    }
}