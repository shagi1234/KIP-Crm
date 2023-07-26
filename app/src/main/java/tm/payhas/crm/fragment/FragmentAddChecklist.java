package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.helpers.StaticMethods.statusBarHeight;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import tm.payhas.crm.api.request.RequestNewChecklist;
import tm.payhas.crm.api.response.ResponseChecklist;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.HelperChecklist;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentAddChecklist extends Fragment implements HelperChecklist {


    private tm.payhas.crm.databinding.FragmentAddChecklistBinding b;
    private static final String ARG_PARAM1 = "taskId";
    private static final String ARG_PARAM2 = "projectId";
    private int taskId;
    private int projectId;
    private AdapterSelectedUsers adapterSelectedUsers;
    private AccountPreferences ac;
    private String timeStart;
    private String timeEnd;
    private ArrayList<Integer> selectedUsersList = new ArrayList<>();

    public static FragmentAddChecklist newInstance(int taskId, int projectId) {
        FragmentAddChecklist fragment = new FragmentAddChecklist();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, taskId);
        args.putInt(ARG_PARAM2, projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getInt(ARG_PARAM1);
            projectId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.main,
                0,
                statusBarHeight,
                0,
                0), 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = tm.payhas.crm.databinding.FragmentAddChecklistBinding.inflate(inflater);
        ac = new AccountPreferences(getContext());
        setRecycler();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        b.btnSave.setOnClickListener(view -> createNewChecklist());
        b.startTime.setOnClickListener(view -> openDialog(b.startTime, 1));
        b.endTime.setOnClickListener(view -> openDialog(b.endTime, 2));
        b.clickableMember.setOnClickListener(view -> addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(FragmentSpinner.TASK_MEMBERS, projectId,taskId,null)));
    }

    private void createNewChecklist() {
        RequestNewChecklist requestNewChecklist = new RequestNewChecklist();
        requestNewChecklist.setName(b.edtName.getText().toString());
        requestNewChecklist.setExecutors(selectedUsersList);
        requestNewChecklist.setFinishesAt(timeEnd);
        requestNewChecklist.setStartsAt(timeStart);
        requestNewChecklist.setTaskId(taskId);
        Call<ResponseChecklist> call = Common.getApi().createNewChecklist(ac.getToken(), requestNewChecklist);
        call.enqueue(new Callback<ResponseChecklist>() {
            @Override
            public void onResponse(Call<ResponseChecklist> call, Response<ResponseChecklist> response) {
                if (response.isSuccessful()) {
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ResponseChecklist> call, Throwable t) {

            }
        });
    }

    private void openDialog(TextView dateSet, int i) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> dateSet.setText(String.valueOf(day1) + "/" + String.valueOf(month1 + 1) + "/" + String.valueOf(year1)), year, month, day);
        datePickerDialog.show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        if (i == 1) {
            timeStart = df.format(c.getTime());
        } else {
            timeEnd = df.format(c.getTime());
        }
    }

    private void setRecycler() {
        adapterSelectedUsers = new AdapterSelectedUsers(getContext(), 2);
        b.membersProject.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.membersProject.setAdapter(adapterSelectedUsers);
    }

    @Override
    public void selectedUsersList(ArrayList<DtoUserInfo> users) {
        adapterSelectedUsers.setSelectedList(users);
        Log.e("TAG", "selectedUsersList: ");
        for (int i = 0; i < users.size(); i++) {
            selectedUsersList.add(users.get(i).getId());
        }
    }
}