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
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.adapters.AdapterListProjects;
import tm.payhas.crm.adapters.AdapterSpinnerUsers;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.api.response.ResponseProjects;
import tm.payhas.crm.api.response.ResponseUsersList;
import tm.payhas.crm.databinding.FragmentSpinnerBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.AddTask;
import tm.payhas.crm.interfaces.HelperAddProject;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentSpinner extends Fragment {
    private FragmentSpinnerBinding b;

    private int type;
    private int projectId;
    public static final int PROJECT_CONNECTED = 1;
    public static final int PROJECTS = 2;
    public static final int RESPONSIBLE = 3;
    public static final int OBSERVERS = 4;
    public static final int PROJECT_MEMBERS = 5;
    public static final int PROJECT_EXECUTOR = 6;
    private AdapterListProjects adapterProjects;
    private AdapterSpinnerUsers adapterProjectUsers;
    private AdapterSpinnerUsers adapterExecutor;
    private AccountPreferences accountPreferences;


    // TODO: Rename and change types and number of parameters
    public static FragmentSpinner newInstance(int type, int projectId) {
        FragmentSpinner fragment = new FragmentSpinner();
        Bundle args = new Bundle();
        args.putInt("roomId", type);
        args.putInt("projectId", projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("roomId");
            projectId = getArguments().getInt("projectId");
        }
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
        b = FragmentSpinnerBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        accountPreferences = new AccountPreferences(getContext());
        setUpSpinner();
        initListeners();
        return b.getRoot();
    }

    private void setUpSpinner() {
        switch (type) {
            case PROJECTS:
                setRecyclerProject();
                getProjects();
                break;
            case PROJECT_CONNECTED:
                getProjectsConnected();
                break;
            case OBSERVERS:
                setRecyclerProjectUsers();
                getObservers();
                break;
            case RESPONSIBLE:
                setRecyclerProjectUsers();
                getResponsible();
                break;
            case PROJECT_MEMBERS:
                setRecyclerProjectUsers();
                getProjectMembers();
                break;
            case PROJECT_EXECUTOR:
                getProjectExecutors();
                Call<ResponseUsersList> call = Common.getApi().getAllUsers();
                call.enqueue(new Callback<ResponseUsersList>() {
                    @Override
                    public void onResponse(Call<ResponseUsersList> call, Response<ResponseUsersList> response) {
                        if (response.isSuccessful()) {
                            adapterExecutor.setUsersList(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUsersList> call, Throwable t) {

                    }
                });

        }

    }

    private void getProjectExecutors() {
        b.rvSpinnerUsers.setVisibility(View.VISIBLE);
        adapterExecutor = new AdapterSpinnerUsers(getContext(), getActivity(), AdapterSpinnerUsers.SINGULAR);
        b.rvSpinnerUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvSpinnerUsers.setAdapter(adapterExecutor);
    }

    private void getProjectMembers() {
        Call<ResponseUsersList> call = Common.getApi().getAllUsers();
        call.enqueue(new Callback<ResponseUsersList>() {
            @Override
            public void onResponse(Call<ResponseUsersList> call, Response<ResponseUsersList> response) {
                if (response.isSuccessful()) {
                    adapterProjectUsers.setUsersList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseUsersList> call, Throwable t) {

            }
        });
    }

    private void getResponsible() {
        getObservers();
    }

    private void getObservers() {
        Call<ResponseUsersList> call = Common.getApi().getProjectsUserList(projectId);
        call.enqueue(new Callback<ResponseUsersList>() {
            @Override
            public void onResponse(Call<ResponseUsersList> call, Response<ResponseUsersList> response) {
                if (response.isSuccessful()) {
                    adapterProjectUsers.setUsersList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseUsersList> call, Throwable t) {

            }
        });
    }

    private void getProjectsConnected() {

    }


    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.customTitleBar,
                0,
                50,
                0,
                0), 100);
    }

    private void initListeners() {
        b.customTitleBar
                .setOnClickListener(view -> {
                    ArrayList<DtoUserInfo> selectedUsers = adapterProjectUsers.getSelectedUsers();
                    getActivity().onBackPressed();

                    if (type == RESPONSIBLE) {
                        Fragment addTask = mainFragmentManager.findFragmentByTag(FragmentAddTask.class.getSimpleName());
                        if (addTask instanceof AddTask) {
                            ((AddTask) addTask).getResponsibleUsers(selectedUsers);
                        }
                    } else if (type == OBSERVERS) {
                        Fragment addTask = mainFragmentManager.findFragmentByTag(FragmentAddTask.class.getSimpleName());
                        if (addTask instanceof AddTask) {
                            ((AddTask) addTask).getObserverUsers(selectedUsers);
                        }
                    }

                    Fragment addProject = mainFragmentManager.findFragmentByTag(FragmentAddProject.class.getSimpleName());
                    if (addProject instanceof HelperAddProject) {
                        ((HelperAddProject) addProject).getProjectUsers(selectedUsers);
                    }


                });
    }

    private void getProjects() {
        b.rvProject.setVisibility(View.VISIBLE);
        Call<ResponseProjects> call = Common.getApi().getAllProjects();
        call.enqueue(new Callback<ResponseProjects>() {
            @Override
            public void onResponse(Call<ResponseProjects> call, Response<ResponseProjects> response) {
                if (response.isSuccessful() || response.body() != null) {
                    adapterProjects.setProjects(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseProjects> call, Throwable t) {

            }
        });

    }

    private void setRecyclerProjectUsers() {
        b.rvSpinnerUsers.setVisibility(View.VISIBLE);
        adapterProjectUsers = new AdapterSpinnerUsers(getContext(), getActivity(), AdapterSpinnerUsers.MULTIPLE);
        b.rvSpinnerUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvSpinnerUsers.setAdapter(adapterProjectUsers);
    }

    private void setRecyclerProject() {
        b.rvProject.setVisibility(View.VISIBLE);
        adapterProjects = new AdapterListProjects(getContext());
        b.rvProject.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvProject.setAdapter(adapterProjects);
        adapterProjects.setActivity(getActivity());
    }
}