package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.helpers.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.payhas.crm.adapters.AdapterChatContact;
import tm.payhas.crm.databinding.FragmentGroupChatBinding;

public class FragmentGroupChat extends Fragment {
    private FragmentGroupChatBinding b;
    private AdapterChatContact adapterChatContact;

    public static FragmentGroupChat newInstance() {
        FragmentGroupChat fragment = new FragmentGroupChat();
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
        b = FragmentGroupChatBinding.inflate(inflater);
        setRecycler();
        return b.getRoot();
    }

    private void setRecycler() {
        adapterChatContact = new AdapterChatContact(getContext());
        b.recGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recGroupContact.setAdapter(adapterChatContact);
    }
}