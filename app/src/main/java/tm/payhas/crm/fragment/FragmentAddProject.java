package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterSelectedUsers;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.api.request.RequestNewProject;
import tm.payhas.crm.api.response.ResponseOneProject;
import tm.payhas.crm.databinding.FragmentAddProjectBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.HelperAddProject;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentAddProject extends Fragment implements HelperAddProject {
    private FragmentAddProjectBinding b;
    private AdapterSelectedUsers adapterSelectedUsers;
    private AdapterSelectedUsers adapterExecutor;
    private ArrayList<Integer> selecctedUserList = new ArrayList<>();
    private String timeStart;
    private String timeEnd;
    private int executorId;

    public static FragmentAddProject newInstance() {
        FragmentAddProject fragment = new FragmentAddProject();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentAddProjectBinding.inflate(inflater);
        initListeners();
        setRecycler();
        addNewProject();
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.main,
                0,
                50,
                0,
                0), 100);
    }

    private void addNewProject() {
        RequestNewProject request = new RequestNewProject();
        request.setName(b.edtNameProject.getText().toString());
        request.setDescription(b.descriptionProject.getText().toString());
        request.setDeadline(timeEnd);
        request.setStartsAt(timeStart);
        request.setProjectParticipants(selecctedUserList);
        request.setExecutorId(executorId);
        Call<ResponseOneProject> call = Common.getApi().addNewProject(AccountPreferences.newInstance(getContext()).getToken(), request);
        call.enqueue(new Callback<ResponseOneProject>() {
            @Override
            public void onResponse(Call<ResponseOneProject> call, Response<ResponseOneProject> response) {
                if (response.isSuccessful()) {
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ResponseOneProject> call, Throwable t) {

            }
        });
    }

    private void setRecycler() {
        adapterSelectedUsers = new AdapterSelectedUsers(getContext(),2);
        b.membersProject.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.membersProject.setAdapter(adapterSelectedUsers);
        adapterExecutor = new AdapterSelectedUsers(getContext(),2);
        b.executorProject.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.executorProject.setAdapter(adapterExecutor);
    }

    private void initListeners() {
        b.btnSave.setOnClickListener(view -> {
            b.btnSave.setEnabled(false);
            addNewProject();
            new Handler().postDelayed(() -> b.btnSave.setEnabled(true), 200);
        });
        b.clickableMember.setOnClickListener(view -> {
            b.clickableMember.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(FragmentSpinner.PROJECT_MEMBERS,0));
            new Handler().postDelayed(() -> b.clickableMember.setEnabled(true), 200);
        });
        b.clickableExecutor.setOnClickListener(view -> {
            b.clickableExecutor.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(FragmentSpinner.PROJECT_EXECUTOR,0));
            new Handler().postDelayed(() -> b.clickableExecutor.setEnabled(true), 200);
        });
        b.projectStartTime.setOnClickListener(view -> openDialog(b.projectStartTime));
        b.projectEndTime.setOnClickListener(view -> openDialog2(b.projectEndTime));

    }

    private void openDialog(TextView dateSet) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> dateSet.setText(String.valueOf(day1) + "/" + String.valueOf(month1 + 1) + "/" + String.valueOf(year1)), year, month, day);
        datePickerDialog.show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        timeStart = df.format(c.getTime());
    }

    private void openDialog2(TextView dateSet) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> dateSet.setText(String.valueOf(day1) + "/" + String.valueOf(month1 + 1) + "/" + String.valueOf(year1)), year, month, day);
        datePickerDialog.show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        timeEnd = df.format(c.getTime());
    }

    @Override
    public void getExecutorUser(DtoUserInfo oneUser) {
        ArrayList<DtoUserInfo> oneUserList = new ArrayList<>();
        oneUserList.add(oneUser);
        executorId = oneUser.getId();
        adapterExecutor.setSelectedList(oneUserList);
    }

    @Override
    public void getProjectUsers(ArrayList<DtoUserInfo> userSelectedList) {
        adapterSelectedUsers.setSelectedList(userSelectedList);

        for (int i = 0; i < userSelectedList.size(); i++) {
            selecctedUserList.add(userSelectedList.get(i).getId());
        }
    }
}