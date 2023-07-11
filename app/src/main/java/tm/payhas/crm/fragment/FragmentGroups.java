package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.adapters.AdapterChatContact;
import tm.payhas.crm.api.response.ResponseUserGroup;
import tm.payhas.crm.dataModels.DataGroup;
import tm.payhas.crm.databinding.FragmentGroupsBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentGroups extends Fragment {
    private FragmentGroupsBinding b;
    private AdapterChatContact adapterChatContact;
    private AccountPreferences ac;
    private ArrayList<DataGroup> groups;

    public static FragmentGroups newInstance() {
        FragmentGroups fragment = new FragmentGroups();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentGroupsBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        ac = new AccountPreferences(getContext());
        getGroups();
        setRecycler();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.addGroupClickable.setOnClickListener(view -> Toast.makeText(getContext(), "Not Yet", Toast.LENGTH_SHORT).show());
    }

    private void getGroups() {
        Call<ResponseUserGroup> call = Common.getApi().getContacts(ac.getToken());
        call.enqueue(new Callback<ResponseUserGroup>() {
            @Override
            public void onResponse(Call<ResponseUserGroup> call, Response<ResponseUserGroup> response) {
                if (response.isSuccessful()) {
                    groups = response.body().getData().getGroups();
                    adapterChatContact.setGroups(groups);
                }
            }

            @Override
            public void onFailure(Call<ResponseUserGroup> call, Throwable t) {

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    private void setRecycler() {
        adapterChatContact = new AdapterChatContact(getContext(), AdapterChatContact.GROUP);
        b.recGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recGroupContact.setAdapter(adapterChatContact);
    }
}