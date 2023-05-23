package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.setPadding;

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
import tm.payhas.crm.adapters.AdapterListProjects;
import tm.payhas.crm.adapters.AdapterSpinnerUsers;
import tm.payhas.crm.api.response.ResponseProjects;
import tm.payhas.crm.databinding.FragmentSpinnerBinding;
import tm.payhas.crm.helpers.Common;

public class FragmentSpinner extends Fragment {
    private FragmentSpinnerBinding b;

    private int type;
    public static int PROJECT_CONNECTED = 1;
    public static int PROJECTS = 2;
    public static int RESPONSIBLE = 3;
    public static int OBSERVERS = 4;
    private AdapterListProjects adapterProjects;
    private AdapterSpinnerUsers adapterProjectUsers;


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
        setRecycler();
        initListeners();
        getProjects();
        return b.getRoot();
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
//        b.customTitleBar
//                .setOnClickListener(view -> {
//                    ArrayList<DataProjectUsers> selectedUsers = adapterSpinner.getSelected();
//                    Fragment addTask = mainFragmentManager.findFragmentByTag(FragmentAddTask.class.getSimpleName());
//                    if (addTask instanceof MultiSelector) {
//                        ((MultiSelector) addTask).selectedUserList(selectedUsers);
//                    }
//                });
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

    private void setRecycler() {
        adapterProjects = new AdapterListProjects(getContext());
        b.rvProject.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvProject.setAdapter(adapterProjects);

        adapterProjectUsers = new AdapterSpinnerUsers(getContext());
        b.rvSpinnerUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvSpinnerUsers.setAdapter(adapterProjectUsers);
    }
}