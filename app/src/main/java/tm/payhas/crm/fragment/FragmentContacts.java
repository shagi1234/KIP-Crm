package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.adapters.AdapterChatContact;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.api.response.ResponseUserGroup;
import tm.payhas.crm.databinding.FragmentContactsBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentContacts extends Fragment {
    private FragmentContactsBinding b;
    private AdapterChatContact adapterChatContact;
    private ArrayList<DtoUserInfo> privateUsers;
    private AccountPreferences accountPreferences;
    private final String TAG = "FragmentChatContacts";

    public static FragmentContacts newInstance() {
        FragmentContacts fragment = new FragmentContacts();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setPrivateUsers(ArrayList<DtoUserInfo> privateUsers) {
        this.privateUsers = privateUsers;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentContactsBinding.inflate(inflater);
        accountPreferences = new AccountPreferences(getContext());
        hideSoftKeyboard(getActivity());
        getPrivateContacts();
        setRecycler();
        initListeners();
        return b.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    private void getPrivateContacts() {
        Call<ResponseUserGroup> call = Common.getApi().getContacts(accountPreferences.getToken());
        call.enqueue(new Callback<ResponseUserGroup>() {
            @Override
            public void onResponse(Call<ResponseUserGroup> call, Response<ResponseUserGroup> response) {
                if (response.isSuccessful()) {
                    adapterChatContact.setPrivateUserList(response.body().getData().getUsersPrivate());
                }
            }

            @Override
            public void onFailure(Call<ResponseUserGroup> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    private void initListeners() {
        b.recGroupContact.setOnClickListener(view -> hideSoftKeyboard(getActivity()));

    }

    private void setRecycler() {
        adapterChatContact = new AdapterChatContact(getContext(), AdapterChatContact.PRIVATE);
        adapterChatContact.setActivity(getActivity());
        b.recGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recGroupContact.setAdapter(adapterChatContact);
    }

}