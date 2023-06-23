package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.Common.normalDate;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.adapters.AdapterSelectedUsers;
import tm.payhas.crm.adapters.AdapterTasks;
import tm.payhas.crm.api.response.ResponseOneProject;
import tm.payhas.crm.dataModels.DataProject;
import tm.payhas.crm.databinding.FragmentOneProjectBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentOneProject extends Fragment {
    private FragmentOneProjectBinding b;
    private int projectId;
    private AccountPreferences ac;
    private int page = 1;
    private final int limit = 10;
    private AdapterTasks adapterTasks;
    private AdapterSelectedUsers adapterSelectedUsers;

    public static FragmentOneProject newInstance(int projectId) {
        FragmentOneProject fragment = new FragmentOneProject();
        Bundle args = new Bundle();
        args.putInt("project_id", projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getInt("project_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentOneProjectBinding.inflate(inflater);
        setUpHelpers();
        getProjectInfo();
        setRecyclers();
        initListeners();
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.main,
                0,
                50,
                0,
                0), 50);
    }

    private void initListeners() {
        b.back.setOnClickListener(view -> getActivity().onBackPressed());
    }

    private void setRecyclers() {
        adapterTasks = new AdapterTasks(getContext());
        b.rvProjectTasks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvProjectTasks.setAdapter(adapterTasks);

        adapterSelectedUsers = new AdapterSelectedUsers(getContext(),2);
        b.rvMembers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.rvMembers.setAdapter(adapterSelectedUsers);
    }

    private void setUpHelpers() {
        ac = new AccountPreferences(getContext());
    }

    private void getProjectInfo() {
        Call<ResponseOneProject> call = Common.getApi().getOneProject(ac.getToken(), projectId, page, limit);
        call.enqueue(new Callback<ResponseOneProject>() {
            @Override
            public void onResponse(Call<ResponseOneProject> call, Response<ResponseOneProject> response) {
                if (response.isSuccessful()) {
                    b.progressBar.setVisibility(View.GONE);
                    b.main.setVisibility(View.VISIBLE);
                    setProjectInfo(response.body().getData());
                    adapterTasks.setTasks(response.body().getData().getTasks());
                    adapterSelectedUsers.setUserList(response.body().getData().getProjectParticipants());
                }
            }

            @Override
            public void onFailure(Call<ResponseOneProject> call, Throwable t) {
                Log.e("Erororor", "onFailure: "+t.getMessage());
            }
        });
    }

    private void setProjectInfo(DataProject data) {
        b.projectEndTime.setText(normalDate(data.getDeadline()));
        b.projectTimeStart.setText(normalDate(data.getStartsAt()));
        b.projectUserCount.setText(String.valueOf(data.getProjectParticipants().size()));
    }
}