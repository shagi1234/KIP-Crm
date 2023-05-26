package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.adapters.AdapterListProjects;
import tm.payhas.crm.adapters.AdapterSpinnerUsers;
import tm.payhas.crm.api.response.ResponseProjects;
import tm.payhas.crm.dataModels.DataProject;
import tm.payhas.crm.dataModels.DataProjectUsers;
import tm.payhas.crm.databinding.FragmentSpinnerBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.AddTask;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentSpinner extends Fragment {
    private FragmentSpinnerBinding b;

    private int type;
    public static final int PROJECT_CONNECTED = 1;
    public static final int PROJECTS = 2;
    public static final int RESPONSIBLE = 3;
    public static final int OBSERVERS = 4;
    private AdapterListProjects adapterProjects;
    private AdapterSpinnerUsers adapterProjectUsers;
    private AccountPreferences accountPreferences;


    // TODO: Rename and change types and number of parameters
    public static FragmentSpinner newInstance(int type) {
        FragmentSpinner fragment = new FragmentSpinner();
        Bundle args = new Bundle();
        args.putInt("roomId", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("roomId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentSpinnerBinding.inflate(inflater);
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
                getResponsible();
                break;

        }
    }

    private void getResponsible() {
    }

    private void getObservers() {
        b.rvProject.setVisibility(View.VISIBLE);
        Call<ResponseProjects> call = Common.getApi().getAllProjects();
        call.enqueue(new Callback<ResponseProjects>() {
            @Override
            public void onResponse(Call<ResponseProjects> call, Response<ResponseProjects> response) {
                if (response.isSuccessful() || response.body() != null) {
                    if (accountPreferences.getPrefCurrentProjectId() == 0) {
                        Toast.makeText(getContext(), "Please Select Category First", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DataProject selectedProject = new DataProject();
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        if (response.body().getData().get(i).getId() == accountPreferences.getPrefCurrentProjectId()) {
                            selectedProject = response.body().getData().get(i);
                        }

                    }
                    adapterProjectUsers.setProjectUsers(selectedProject.getProjectParticipants());
                }
            }

            @Override
            public void onFailure(Call<ResponseProjects> call, Throwable t) {

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
                    if (type == OBSERVERS) {
                        ArrayList<DataProjectUsers> selectedUsers = adapterProjectUsers.getSelectedUsers();
                        ArrayList<Integer> userList = new ArrayList<>();
                        for (int i = 0; i < selectedUsers.size(); i++) {
                            userList.add(selectedUsers.get(i).getUser().getId());
                        }
                        getActivity().onBackPressed();
                        Log.e("UserListTest", "initListeners: " + userList.size());
                        Fragment cloudFolder = mainFragmentManager.findFragmentByTag(FragmentAddTask.class.getSimpleName());
                        if (cloudFolder instanceof AddTask) {
                            ((AddTask) cloudFolder).selectedObserverList(userList);
                        }

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
        adapterProjectUsers = new AdapterSpinnerUsers(getContext());
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