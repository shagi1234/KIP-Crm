package tm.payhas.crm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.payhas.crm.adapters.AdapterChatContact;
import tm.payhas.crm.databinding.FragmentGroupsBinding;

public class FragmentGroups extends Fragment {
    private FragmentGroupsBinding b;
    private AdapterChatContact adapterChatContact;

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
        setRecycler();
        return b.getRoot();
    }

    private void setRecycler() {
        adapterChatContact = new AdapterChatContact(getContext());
        b.recGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recGroupContact.setAdapter(adapterChatContact);
    }
}