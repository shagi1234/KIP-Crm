package tm.payhas.crm.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterProjects;
import tm.payhas.crm.databinding.FragmentProjectsBinding;

public class FragmentProjects extends Fragment {
    private FragmentProjectsBinding b;
    private AdapterProjects adapterProjects;

    public static FragmentProjects newInstance() {
        FragmentProjects fragment = new FragmentProjects();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b= FragmentProjectsBinding.inflate(inflater);
        setRecycler();
        return b.getRoot();
    }

    private void setRecycler() {
        adapterProjects = new AdapterProjects(getContext());
        b.rcvProjects.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        b.rcvProjects.setAdapter(adapterProjects);
    }
}