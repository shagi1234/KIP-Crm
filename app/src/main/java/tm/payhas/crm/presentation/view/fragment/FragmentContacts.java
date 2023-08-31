package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentContactsBinding;
import tm.payhas.crm.domain.helpers.NetworkConnectivityUtil;
import tm.payhas.crm.domain.interfaces.NetworkChangeListener;
import tm.payhas.crm.presentation.view.adapters.AdapterChatContact;

import tm.payhas.crm.presentation.viewModel.ViewModelUser;

public class FragmentContacts extends Fragment implements NetworkChangeListener {
    private FragmentContactsBinding b;
    private AdapterChatContact adapterChatContact;
    private ViewModelUser viewModelUser;
    private final String TAG = "FragmentChatContacts";
    private NetworkConnectivityUtil connectivityUtil;


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
        setNetworkObserver();
        return b.getRoot();


    }

    private void setNetworkObserver() {
        connectivityUtil = new NetworkConnectivityUtil(getContext(), this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        connectivityUtil.unregisterReceiver(requireContext());
    }

    @Override
    public void onNetworkConnected() {
        NetworkChangeListener.super.onNetworkConnected();
    }

    @Override
    public void onNetworkDisconnected() {
        NetworkChangeListener.super.onNetworkDisconnected();
    }
}