package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.statics.StaticConstants.IN_PROCESS;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.api.request.RequestCreateTask;
import tm.payhas.crm.api.response.ResponseTasks;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentAddTask extends Fragment {
    private tm.payhas.crm.databinding.FragmentAddTaskBinding b;
    private AccountPreferences accountPreferences;

    public static FragmentAddTask newInstance() {
        FragmentAddTask fragment = new FragmentAddTask();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = tm.payhas.crm.databinding.FragmentAddTaskBinding.inflate(inflater);
        initListeners();
        accountPreferences = new AccountPreferences(getContext());
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.mainContent,
                0,
                50,
                0,
                0), 100);
    }


    private void initListeners() {
        b.timeReminder.setOnClickListener(view -> Toast.makeText(getContext(), "CLicked", Toast.LENGTH_SHORT).show());
        b.timeEnd.setOnClickListener(view -> Toast.makeText(getContext(), "CLicked", Toast.LENGTH_SHORT).show());
        b.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });
    }

    private void createTask() {
        ArrayList<Integer> observerTeacher = new ArrayList<>();
        observerTeacher.add(1);
        observerTeacher.add(2);
        observerTeacher.add(3);
        observerTeacher.add(4);
        ArrayList<String> remindAt = new ArrayList<>();
        remindAt.add("2023-04-10T12:00");
        remindAt.add("2023-04-10T12:00");
        remindAt.add("2023-04-10T12:00");
        remindAt.add("2023-04-10T12:00");
        String name = b.edtTaskName.getText().toString();
        String info = b.infoInput.getText().toString();
        RequestCreateTask requestCreateTask = new RequestCreateTask();
        requestCreateTask.setName(name);
        requestCreateTask.setProjectId(null);
        requestCreateTask.setName(info);
        requestCreateTask.setRemindAt(remindAt);
        requestCreateTask.setReminderType("author");
        requestCreateTask.setStartsAt("2023-03-30");
        requestCreateTask.setFinishesAt("2023-03-30");
        requestCreateTask.setObserverUsers(observerTeacher);
        requestCreateTask.setResponsibleUsers(observerTeacher);
        requestCreateTask.setPriority("primary");
        requestCreateTask.setDescription("slfjaskhfuagudjahfywjhdajkugduyfajsfus");
        requestCreateTask.setStatus(IN_PROCESS);
        requestCreateTask.setAuthorId(1);


        Call<ResponseTasks> call = Common.getApi().createTask(accountPreferences.getToken(), requestCreateTask);
        call.enqueue(new Callback<ResponseTasks>() {
            @Override
            public void onResponse(Call<ResponseTasks> call, Response<ResponseTasks> response) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }

            }

            @Override
            public void onFailure(Call<ResponseTasks> call, Throwable t) {

            }
        });
    }
}