package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.domain.helpers.Common.menuBar;
import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.domain.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.domain.helpers.StaticMethods.statusBarHeight;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentFlowBinding;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.presentation.view.activity.ActivityLoginRegister;

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
        new Handler().postDelayed(() -> setPadding(b.secondaryContent,
                0,
                statusBarHeight,
                0,
                0), 100);
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
        Common.hideAdd(FragmentMessages.newInstance(), FragmentMessages.class.getSimpleName(), ActivityLoginRegister.mainFragmentManager, R.id.secondary_content);
        b.bottomNavigationBar.setSelectedItemId(R.id.chat);
    }

    @SuppressLint("NonConstantResourceId")
    private void initListeners() {
        menuBar=b.bottomNavigationBar;
        b.bottomNavigationBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Common.hideAdd(FragmentHome.newInstance(), FragmentHome.class.getSimpleName(), ActivityLoginRegister.mainFragmentManager, R.id.secondary_content);
                    break;
                case R.id.chat:
                    Common.hideAdd(FragmentMessages.newInstance(), FragmentMessages.class.getSimpleName(), ActivityLoginRegister.mainFragmentManager, R.id.secondary_content);
                    break;
                case R.id.document:
                    Common.hideAdd(FragmentProjectTasks.newInstance(), FragmentProjectTasks.class.getSimpleName(), ActivityLoginRegister.mainFragmentManager, R.id.secondary_content);
                    break;
                case R.id.document_cloud:
                    Common.hideAdd(FragmentCloudFolder.newInstance(), FragmentCloudFolder.class.getSimpleName(), ActivityLoginRegister.mainFragmentManager, R.id.secondary_content);
                    break;
                case R.id.profile:
                    Common.hideAdd(FragmentProfile.newInstance(), FragmentProfile.class.getSimpleName(), ActivityLoginRegister.mainFragmentManager, R.id.secondary_content);
                    break;
            }
            return true;
        });
    }
}
