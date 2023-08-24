package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.presentation.view.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.Common.normalDate;
import static tm.payhas.crm.domain.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.domain.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.domain.helpers.StaticMethods.statusBarHeight;
import static tm.payhas.crm.domain.statics.StaticConstants.FINISHED;
import static tm.payhas.crm.domain.statics.StaticConstants.IN_PROCESS;
import static tm.payhas.crm.domain.statics.StaticConstants.NOT_STARTED;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentOneProjectBinding;
import tm.payhas.crm.presentation.view.adapters.AdapterSelectedUsers;
import tm.payhas.crm.presentation.view.adapters.AdapterTasks;
import tm.payhas.crm.data.remote.api.response.ResponseOneProject;
import tm.payhas.crm.domain.model.DataProject;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.domain.interfaces.OnInternetStatus;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

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
        new Handler().postDelayed(() -> setPadding(b.swiper,
                0,
                statusBarHeight,
                0,
                0), 50);
    }

    private void initListeners() {
        b.swiper.setOnRefreshListener(() -> {
            b.swiper.setRefreshing(true);
            getProjectInfo();
        });
        b.cancelTaskClicker.setOnClickListener(view -> changeProjectStatus());
        b.back.setOnClickListener(view -> getActivity().onBackPressed());
        b.editProject.setOnClickListener(view -> {
            b.editProject.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentAddProject.newInstance(true, projectId));
            new Handler().postDelayed(() -> b.editProject.setEnabled(true), 200);
        });
        b.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.addTask.setEnabled(false);
                addFragment(mainFragmentManager, R.id.main_content, FragmentAddTask.newInstance(projectId, 0));
                new Handler().postDelayed(() -> b.addTask.setEnabled(true), 200);
            }
        });
    }


    private void setStatus(String statusReceived) {
        Activity activity = getActivity();
        Context context = getContext();
        switch (statusReceived) {
            case IN_PROCESS:
                setBackgroundDrawable(context, b.projectStatus, R.color.status_in_process, 0, 50, false, 0);
                b.projectStatus.setTextColor(activity.getResources().getColor(R.color.status_in_process_text));
                b.projectStatus.setText(R.string.in_process);
                break;
            case NOT_STARTED:
                setBackgroundDrawable(context, b.projectStatus, R.color.status_not_started, 0, 50, false, 0);
                b.projectStatus.setTextColor(activity.getResources().getColor(R.color.status_not_started_text));
                b.projectStatus.setText(R.string.not_started);
                break;
            case FINISHED:
                setBackgroundDrawable(context, b.projectStatus, R.color.status_finished, 0, 50, false, 0);
                b.projectStatus.setTextColor(activity.getResources().getColor(R.color.status_finished_text));
                b.projectStatus.setText(R.string.finished);
                break;
        }
    }

    private void changeProjectStatus() {
        Call<ResponseOneProject> call = Common.getApi().changeProjectsStatus(ac.getToken(), projectId);
        call.enqueue(new Callback<ResponseOneProject>() {
            @Override
            public void onResponse(Call<ResponseOneProject> call, Response<ResponseOneProject> response) {
                if (response.isSuccessful()) {
                    getProjectInfo();
                }
            }

            @Override
            public void onFailure(Call<ResponseOneProject> call, Throwable t) {

            }
        });
    }

    private void setExecutorButton(DataProject data) {
        if (data.isExecutor() || data.isAuthor()) {
            b.projectStatusChanger.setVisibility(View.VISIBLE);
        } else {
            b.projectStatusChanger.setVisibility(View.GONE);
        }
        if (data.getStatus().equals(IN_PROCESS)) {
            b.cancelTask.setText(R.string.finish);
        } else if (data.getStatus().equals(NOT_STARTED)) {
            b.cancelTask.setText(R.string.start);
        } else {
            b.projectStatusChanger.setVisibility(View.GONE);
        }

    }

    private void setRecyclers() {
        adapterTasks = new AdapterTasks(getContext(), getActivity());
        b.rvProjectTasks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvProjectTasks.setAdapter(adapterTasks);

        adapterSelectedUsers = new AdapterSelectedUsers(getContext(), 1);
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
                    b.swiper.setRefreshing(false);
                    OnInternetStatus internetStatusListener = new OnInternetStatus() {
                    };
                    internetStatusListener.setConnected(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
                    b.main.setVisibility(View.VISIBLE);
                    setProjectInfo(response.body().getData());
                    adapterTasks.setTasks(response.body().getData().getTasks());
                    adapterSelectedUsers.setUserList(response.body().getData().getProjectParticipants());
                    setExecutorButton(response.body().getData());
                    setStatus(response.body().getData().getStatus());

                }
            }

            @Override
            public void onFailure(Call<ResponseOneProject> call, Throwable t) {
                b.swiper.setRefreshing(false);
                OnInternetStatus internetStatusListener = new OnInternetStatus() {
                };
                internetStatusListener.setConnected(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
            }
        });
    }

    private void setProjectInfo(DataProject data) {
        b.projectEndTime.setText(normalDate(data.getDeadline()));
        b.projectTimeStart.setText(normalDate(data.getStartsAt()));
        b.projectUserCount.setText(String.valueOf(data.getProjectParticipants().size()));
        b.projectRemainingDays.setText(String.valueOf(data.getTimeOut()));
    }
}