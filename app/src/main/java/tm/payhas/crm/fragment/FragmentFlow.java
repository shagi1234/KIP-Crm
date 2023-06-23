package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityLoginRegister.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.menuBar;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentFlowBinding;
import tm.payhas.crm.helpers.Common;

public class FragmentFlow extends Fragment {
    private FragmentFlowBinding b;

    public static FragmentFlow newInstance() {
        FragmentFlow fragment = new FragmentFlow();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentFlowBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        setContent();
        initListeners();
        return b.getRoot();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    private void setContent() {
        Common.hideAdd(FragmentHome.newInstance(), FragmentHome.class.getSimpleName(), mainFragmentManager, R.id.secondary_content);
        b.bottomNavigationBar.setSelectedItemId(R.id.home);
    }

    @SuppressLint("NonConstantResourceId")
    private void initListeners() {
        menuBar=b.bottomNavigationBar;
        b.bottomNavigationBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Common.hideAdd(FragmentHome.newInstance(), FragmentHome.class.getSimpleName(), mainFragmentManager, R.id.secondary_content);
                    break;
                case R.id.chat:
                    Common.hideAdd(FragmentMessages.newInstance(), FragmentMessages.class.getSimpleName(), mainFragmentManager, R.id.secondary_content);
                    break;
                case R.id.document:
                    Common.hideAdd(FragmentProjectTasks.newInstance(), FragmentProjectTasks.class.getSimpleName(), mainFragmentManager, R.id.secondary_content);
                    break;
                case R.id.document_cloud:
                    Common.hideAdd(FragmentCloudFolder.newInstance(), FragmentCloudFolder.class.getSimpleName(), mainFragmentManager, R.id.secondary_content);
                    break;
                case R.id.profile:
                    Common.hideAdd(FragmentProfile.newInstance(), FragmentProfile.class.getSimpleName(), mainFragmentManager, R.id.secondary_content);
                    break;
            }
            return true;
        });
    }
}
