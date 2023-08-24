package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.presentation.view.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.domain.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentProjectTasksBinding;
import tm.payhas.crm.presentation.view.adapters.AdapterViewPager;


public class FragmentProjectTasks extends Fragment {

    private AdapterViewPager adapterViewPager;
    private FragmentProjectTasksBinding b;

    public static FragmentProjectTasks newInstance() {
        FragmentProjectTasks fragment = new FragmentProjectTasks();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProjectTasksBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        setViewPager();
        setupTabIcons();
        return b.getRoot();
    }


    private void setViewPager() {
        adapterViewPager = new AdapterViewPager(mainFragmentManager);
        adapterViewPager.addFragment(new FragmentTasks(),getActivity().getResources().getString(R.string.tasks) );
        adapterViewPager.addFragment(new FragmentProjects(),getActivity().getResources().getString(R.string.projects) );
        b.tabsProjectTasks.setupWithViewPager(b.vpProjectTasks);
        b.vpProjectTasks.setAdapter(adapterViewPager);
    }

    private void setupTabIcons() {
        b.tabsProjectTasks.getTabAt(0).setIcon(R.drawable.ic_tasks);
        b.tabsProjectTasks.getTabAt(1).setIcon(R.drawable.ic_projects);
    }
}