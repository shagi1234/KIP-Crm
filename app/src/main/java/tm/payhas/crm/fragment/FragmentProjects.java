package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterProjects;
import tm.payhas.crm.api.response.ResponseProjects;
import tm.payhas.crm.databinding.FragmentProjectsBinding;
import tm.payhas.crm.helpers.Common;

public class FragmentProjects extends Fragment {
    private FragmentProjectsBinding b;
    private AdapterProjects adapterProjects;
    private static final String TAG = "FRAGMENT_PROJECTS";

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentProjectsBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        setRecycler();
        initListeners();
        getProjects();
        return b.getRoot();
    }

    private void initListeners() {
        b.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProjects();
            }
        });
        b.favAdd.setOnClickListener(view -> {
            b.favAdd.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentAddProject.newInstance());
            new Handler().postDelayed(() -> b.favAdd.setEnabled(true), 200);
        });
    }

    private void getProjects() {
        Call<ResponseProjects> call = Common.getApi().getAllProjects();
        call.enqueue(new Callback<ResponseProjects>() {
            @Override
            public void onResponse(Call<ResponseProjects> call, Response<ResponseProjects> response) {
                if (response.isSuccessful() || response.body() != null) {
                    adapterProjects.setProjects(response.body().getData());
                    b.main.setVisibility(View.VISIBLE);
                    b.progressBar.setVisibility(View.GONE);
                    b.swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseProjects> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                b.main.setVisibility(View.VISIBLE);
                b.progressBar.setVisibility(View.GONE);
                b.swipe.setRefreshing(false);
            }
        });

    }

    private void setRecycler() {
        adapterProjects = new AdapterProjects(getContext());
        b.rcvProjects.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rcvProjects.setAdapter(adapterProjects);
    }
}