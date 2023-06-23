package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterViewPager;
import tm.payhas.crm.databinding.FragmentProjectTasksBinding;


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
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.mainContent,
                0,
                50,
                0,
                0), 100);
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
        adapterViewPager.addFragment(new FragmentTasks(), "задачи");
        adapterViewPager.addFragment(new FragmentProjects(), "Проекты");
        b.tabsProjectTasks.setupWithViewPager(b.vpProjectTasks);
        b.vpProjectTasks.setAdapter(adapterViewPager);
    }

    private void setupTabIcons() {
        b.tabsProjectTasks.getTabAt(0).setIcon(R.drawable.ic_tasks);
        b.tabsProjectTasks.getTabAt(1).setIcon(R.drawable.ic_projects);
    }
}