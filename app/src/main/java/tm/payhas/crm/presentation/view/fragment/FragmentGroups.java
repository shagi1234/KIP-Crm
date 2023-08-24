package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.presentation.view.activity.ActivityMain.mainFragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentGroupsBinding;
import tm.payhas.crm.presentation.view.adapters.AdapterChatContact;
import tm.payhas.crm.domain.interfaces.OnRefresh;
import tm.payhas.crm.presentation.viewModel.ViewModelGroup;

public class FragmentGroups extends Fragment implements OnRefresh {
    private FragmentGroupsBinding b;
    private AdapterChatContact adapterChatContact;
    private ViewModelGroup viewModelGroup;

    public static FragmentGroups newInstance() {
        FragmentGroups fragment = new FragmentGroups();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_groups, container, false);
        setViewModel();
        hideSoftKeyboard(getActivity());
        setRecycler();
        initListeners();
        getGroups();
        return b.getRoot();
    }

    private void setViewModel() {
        viewModelGroup = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ViewModelGroup.class);
        b.setViewModel(viewModelGroup);
        b.setLifecycleOwner(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initListeners() {
        b.addGroupClickable.setOnClickListener(view -> {
            b.addGroupClickable.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentCreateGroup.newInstance());
            new Handler().postDelayed(() -> b.addGroupClickable.setEnabled(true), 200);
        });
    }

    private void getGroups() {
        viewModelGroup.connectAndUpdate();
        try {
            viewModelGroup.getAllGroups().observe(getViewLifecycleOwner(), entityGroups -> adapterChatContact.setGroups(entityGroups));
        } catch (Exception e) {
            Log.e("FragmetGroups", "getGroups: " + e);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
        getGroups();
    }

    private void setRecycler() {
        adapterChatContact = new AdapterChatContact(AdapterChatContact.GROUP);
        b.recGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recGroupContact.setAdapter(adapterChatContact);
    }

    @Override
    public void refresh() {
        getGroups();
    }
}