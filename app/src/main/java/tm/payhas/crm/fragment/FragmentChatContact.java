package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.payhas.crm.adapters.AdapterChatContact;
import tm.payhas.crm.databinding.FragmentChatContactBinding;

public class FragmentChatContact extends Fragment {
    private FragmentChatContactBinding b;
    private AdapterChatContact adapterChatContact;

    public static FragmentChatContact newInstance() {
        FragmentChatContact fragment = new FragmentChatContact();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentChatContactBinding.inflate(inflater);
        setRecycler();
        initListeners();
        return b.getRoot();
    }


    private void initListeners() {
        b.recGroupContact.setOnClickListener(view -> hideSoftKeyboard(getActivity()));

    }

    private void setRecycler() {

        adapterChatContact = new AdapterChatContact(getContext());
        adapterChatContact.setActivity(getActivity());
        b.recGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recGroupContact.setAdapter(adapterChatContact);
    }

}