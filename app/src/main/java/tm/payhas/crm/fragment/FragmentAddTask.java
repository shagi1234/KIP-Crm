package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.fragment.FragmentSpinner.OBSERVERS;
import static tm.payhas.crm.fragment.FragmentSpinner.PROJECTS;
import static tm.payhas.crm.fragment.FragmentSpinner.RESPONSIBLE;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.statics.StaticConstants.FINISHED;
import static tm.payhas.crm.statics.StaticConstants.HIGH;
import static tm.payhas.crm.statics.StaticConstants.IN_PROCESS;
import static tm.payhas.crm.statics.StaticConstants.MEDIUM;
import static tm.payhas.crm.statics.StaticConstants.NOT_IMPORTANT;
import static tm.payhas.crm.statics.StaticConstants.NOT_STARTED;
import static tm.payhas.crm.statics.StaticConstants.PENDING;
import static tm.payhas.crm.statics.StaticConstants.PRIMARY;
import static tm.payhas.crm.statics.StaticConstants.REVIEW;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.api.request.RequestCreateTask;
import tm.payhas.crm.api.response.ResponseTasks;
import tm.payhas.crm.dataModels.DataProject;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.AddTask;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentAddTask extends Fragment implements AddTask {
    private tm.payhas.crm.databinding.FragmentAddTaskBinding b;
    private AccountPreferences accountPreferences;
    private String status;
    private String importancy;
    private int projectId = 0;
    private ArrayList<Integer> selectedObserverList = new ArrayList<>();

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
        setUpSpinners();
        initListeners();
        accountPreferences = new AccountPreferences(getContext());
        return b.getRoot();
    }

    private void setUpSpinners() {
        b.spinnerTaskStatus.setVisibility(View.GONE);
        String[] statuses = getResources().getStringArray(R.array.status);
        String[] importancy = getResources().getStringArray(R.array.importancy);
        ArrayAdapter adapterStatus = new ArrayAdapter(getContext(), R.layout.item_spinner, statuses);
        ArrayAdapter adapterImportancy = new ArrayAdapter(getContext(), R.layout.item_spinner, importancy);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterImportancy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnerTaskImportancy.setAdapter(adapterImportancy);
        b.spinnerTaskStatus.setAdapter(adapterStatus);
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
        b.vTaskProjects.setOnClickListener(view -> {
            b.vTaskProjects.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(PROJECTS));
            new Handler().postDelayed(() -> b.vTaskProjects.setEnabled(true), 200);
        });
        b.vObservers.setOnClickListener(view -> {
            b.vObservers.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(OBSERVERS));
            new Handler().postDelayed(() -> b.vObservers.setEnabled(true), 200);

        });
        b.vResponsible.setOnClickListener(view -> {
            b.vObservers.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(RESPONSIBLE));
            new Handler().postDelayed(() -> b.vObservers.setEnabled(true), 200);
        });
        b.spinnerTaskStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                String toSend;
                switch (selected) {
                    case "Не начата":
                        toSend = NOT_STARTED;
                        break;
                    case "В процессе":
                        toSend = IN_PROCESS;
                        break;
                    case "На проверке":
                        toSend = REVIEW;
                        break;
                    case "В ожидании ответашенo":
                        toSend = PENDING;
                        break;
                    case "Завершенo":
                        toSend = FINISHED;
                        break;

                }
                toSend = status;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        b.spinnerTaskImportancy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selected = adapterView.getItemAtPosition(i).toString();
                String toSend;
                switch (selected) {
                    case "Низкий":
                        toSend = PRIMARY;
                        break;
                    case "Средний":
                        toSend = MEDIUM;
                        break;
                    case "Высокий":
                        toSend = HIGH;
                        break;
                    case "Неотложный":
                        toSend = NOT_IMPORTANT;
                        break;
                }
                toSend = importancy;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        ArrayList<String> remindAt = new ArrayList<>();
        remindAt.add("2023-04-10T12:00");
        remindAt.add("2023-04-10T12:00");
        remindAt.add("2023-04-10T12:00");
        remindAt.add("2023-04-10T12:00");
        String name = b.edtTaskName.getText().toString();
        String info = b.infoInput.getText().toString();
        RequestCreateTask requestCreateTask = new RequestCreateTask();
        requestCreateTask.setObserverUsers(selectedObserverList);
        requestCreateTask.setName(name);
        requestCreateTask.setName(info);
        requestCreateTask.setRemindAt(remindAt);
        requestCreateTask.setReminderType("author");
        requestCreateTask.setStartsAt("2023-03-30");
        requestCreateTask.setFinishesAt("2023-03-30");
        requestCreateTask.setProjectId(projectId);
        requestCreateTask.setPriority(importancy);
        requestCreateTask.setDescription("slfjaskhfuagudjahfywjhdajkugduyfajsfus");
        requestCreateTask.setStatus(status);
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

    @Override
    public void selectedProjectId(DataProject oneProject) {
        projectId = oneProject.getId();
        accountPreferences.setPrefCurrentProjectId(oneProject.getId());
        b.vTaskProjects.setText(oneProject.getName());
    }

    @Override
    public void selectedObserverList(ArrayList<Integer> userList) {
        selectedObserverList = userList;
    }
}