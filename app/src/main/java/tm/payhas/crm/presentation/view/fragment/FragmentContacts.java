package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.databinding.FragmentContactsBinding;
import tm.payhas.crm.presentation.view.adapters.AdapterChatContact;

import tm.payhas.crm.presentation.viewModel.ViewModelUser;

public class FragmentContacts extends Fragment {
    private FragmentContactsBinding b;
    private AdapterChatContact adapterChatContact;
    private ViewModelUser viewModelUser;
    private final String TAG = "FragmentChatContacts";

    public static FragmentContacts newInstance() {
        FragmentContacts fragment = new FragmentContacts();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false);
        setViewModel();
        hideSoftKeyboard(getActivity());
        getPrivateContacts();
        setRecycler();
        initListeners();
        return b.getRoot();


    }

    private void setViewModel() {
        viewModelUser = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ViewModelUser.class);
        b.setViewModel(viewModelUser);
        b.setLifecycleOwner(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    private void getPrivateContacts() {
        viewModelUser.getConnectedAndUpdate();
        try {
            viewModelUser.getAllUsers().observe(getViewLifecycleOwner(), entityUserInfos -> adapterChatContact.setPrivateUserList(entityUserInfos));
        } catch (Exception e) {
            Log.e("FragmetGroups", "getGroups: " + e);
        }
    }


    private void initListeners() {
        b.recGroupContact.setOnClickListener(view -> hideSoftKeyboard(getActivity()));
    }

    private void setRecycler() {
        adapterChatContact = new AdapterChatContact(AdapterChatContact.PRIVATE);
        adapterChatContact.setActivity(getActivity());
        b.recGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recGroupContact.setAdapter(adapterChatContact);
    }

}