package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.fragment.FragmentSpinner.OBSERVERS;
import static tm.payhas.crm.fragment.FragmentSpinner.PROJECTS;
import static tm.payhas.crm.fragment.FragmentSpinner.PROJECT_EXECUTOR;
import static tm.payhas.crm.fragment.FragmentSpinner.RESPONSIBLE;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.FileHelper.getRealPathFromURI;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
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

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterSelectedUsers;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.api.request.RequestCreateTask;
import tm.payhas.crm.api.response.ResponseSingleFile;
import tm.payhas.crm.api.response.ResponseTasks;
import tm.payhas.crm.dataModels.DataProject;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.AddTask;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentAddTask extends Fragment implements AddTask {
    private tm.payhas.crm.databinding.FragmentAddTaskBinding b;
    private static final int REQUEST_CODE = 112211;
    private AccountPreferences accountPreferences;
    private String importancy = NOT_IMPORTANT;
    private int projectId = 0;
    private int executorId;
    private ArrayList<Integer> responsibleUsersList = new ArrayList<>();
    private ArrayList<Integer> observerUserList = new ArrayList<>();
    private String reminderTypeToSend = "";
    private String taskStatusToSend = "";
    private String timeEnd;
    private String timeStart;
    private String timeRemind;
    private AdapterSelectedUsers adapterObserver;
    private AdapterSelectedUsers adapterResponsible;
    private String fileUrl;

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
        b = tm.payhas.crm.databinding.FragmentAddTaskBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        setUpSpinners();
        initListeners();
        setRecyclers();
        accountPreferences = new AccountPreferences(getContext());
        accountPreferences.setPrefCurrentProjectId(0);
        return b.getRoot();
    }

    private void setRecyclers() {
        adapterObserver = new AdapterSelectedUsers(getContext(), 2);
        b.taskObservers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.taskObservers.setAdapter(adapterObserver);

        adapterResponsible = new AdapterSelectedUsers(getContext(), 2);
        b.taskResponseible.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.taskResponseible.setAdapter(adapterResponsible);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    private void setUpSpinners() {
        b.spinnerTaskStatus.setVisibility(View.GONE);
        String[] statuses = getResources().getStringArray(R.array.status);
        String[] importancy = getResources().getStringArray(R.array.importancy);
        String[] reminderType = getResources().getStringArray(R.array.reminder_type);
        ArrayAdapter adapterReminderType = new ArrayAdapter(getContext(), R.layout.item_spinner, reminderType);
        ArrayAdapter adapterStatus = new ArrayAdapter(getContext(), R.layout.item_spinner, statuses);
        ArrayAdapter adapterImportancy = new ArrayAdapter(getContext(), R.layout.item_spinner, importancy);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterReminderType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterImportancy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnerTaskImportancy.setAdapter(adapterImportancy);
        b.spinnerReminderType.setAdapter(adapterReminderType);
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

    private void pickFileFromInternalStorage() {
        requestStoragePermission();
        b.fileMain.setVisibility(View.GONE);
        b.progressAddFile.setVisibility(View.VISIBLE);

        String[] mimeTypes = {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                "text/plain", "application/pdf", "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE);
    }

    private void initListeners() {
        b.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFileFromInternalStorage();
            }
        });
        b.timeEnd.setOnClickListener(view -> openDialog(b.timeEnd, 1));
        b.timeReminder.setOnClickListener(view -> openDialog(b.timeReminder, 2));
        b.timeStart.setOnClickListener(view -> openDialog(b.timeStart, 3));
        b.vTaskProjects.setOnClickListener(view -> {
            b.vTaskProjects.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(PROJECTS, accountPreferences.getPrefCurrentProjectId()));
            new Handler().postDelayed(() -> b.vTaskProjects.setEnabled(true), 200);
        });
        b.clickerObserver.setOnClickListener(view -> {
            b.clickerObserver.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(OBSERVERS, accountPreferences.getPrefCurrentProjectId()));
            new Handler().postDelayed(() -> b.clickerObserver.setEnabled(true), 200);

        });
        b.taskExecutor.setOnClickListener(view -> {
            b.taskExecutor.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(PROJECT_EXECUTOR, accountPreferences.getPrefCurrentProjectId()));
            new Handler().postDelayed(() -> b.taskExecutor.setEnabled(true), 200);
        });
        b.clickerResponsible.setOnClickListener(view -> {
            b.clickerResponsible.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(RESPONSIBLE, accountPreferences.getPrefCurrentProjectId()));
            new Handler().postDelayed(() -> b.clickerResponsible.setEnabled(true), 200);
        });
        b.spinnerReminderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                String toSend;
                switch (selected) {
                    case "Автор":
                        toSend = "author";
                        toSend = reminderTypeToSend;
                        break;
                    case "Наблюдатели":
                        toSend = "observerUsers";
                        toSend = reminderTypeToSend;
                        break;
                    case "Ответственные":
                        toSend = "responsibleUsers";
                        toSend = reminderTypeToSend;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        b.spinnerTaskStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                String toSend;
                switch (selected) {
                    case "Не начата":
                        toSend = NOT_STARTED;
                        toSend = taskStatusToSend;
                        break;
                    case "В процессе":
                        toSend = IN_PROCESS;
                        toSend = taskStatusToSend;
                        break;
                    case "На проверке":
                        toSend = REVIEW;
                        toSend = taskStatusToSend;
                        break;
                    case "В ожидании ответашенo":
                        toSend = PENDING;
                        toSend = taskStatusToSend;
                        break;
                    case "Завершенo":
                        toSend = FINISHED;
                        toSend = taskStatusToSend;
                        break;


                }
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
                        toSend = importancy;
                        break;
                    case "Средний":
                        toSend = MEDIUM;
                        toSend = importancy;
                        break;
                    case "Высокий":
                        toSend = HIGH;
                        toSend = importancy;
                        break;
                    case "Неотложный":
                        toSend = NOT_IMPORTANT;
                        toSend = importancy;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        b.btnSave.setOnClickListener(view -> createTask());
    }

    private void openDialog(TextView dateSet, int type) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> dateSet.setText(String.valueOf(day1) + "/" + String.valueOf(month1 + 1) + "/" + String.valueOf(year1)), year, month, day);
        datePickerDialog.show();
        if (type == 1) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            timeEnd = df.format(c.getTime());
        } else if (type == 2) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            timeRemind = df.format(c.getTime());
        } else if (type == 3) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            timeStart = df.format(c.getTime());
        }
    }

    private void createTask() {
        ArrayList<String> remindAt = new ArrayList<>();
        remindAt.add(timeRemind);
        RequestCreateTask requestCreateTask = new RequestCreateTask();
        requestCreateTask.setName(b.edtTaskName.getText().toString());
        requestCreateTask.setProjectId(accountPreferences.getPrefCurrentProjectId());
        requestCreateTask.setPriority(importancy);
        requestCreateTask.setExecutorId(executorId);
        requestCreateTask.setRemindAt(remindAt);
        requestCreateTask.setStartsAt(timeStart);
        requestCreateTask.setFinishesAt(timeEnd);
        requestCreateTask.setResponsibleUsers(responsibleUsersList);
        requestCreateTask.setObserverUsers(observerUserList);
        requestCreateTask.setDescription(b.infoInput.getText().toString());
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
    public void setExecutor(DtoUserInfo user) {
        b.taskExecutor.setText(user.getPersonalData().getName() + user.getPersonalData().getSurname());
        executorId = user.getId();
    }

    @Override
    public void getResponsibleUsers(ArrayList<DtoUserInfo> users) {
        Log.e("ADD_TASK", "getResponsibleUsers: " + users.size());
        adapterResponsible.setSelectedList(users);

        for (int i = 0; i < users.size(); i++) {
            responsibleUsersList.add(users.get(i).getId());
        }
    }

    @Override
    public void getObserverUsers(ArrayList<DtoUserInfo> users) {
        Log.e("ADD_TASK", "getObserverUsers: " + users.size());
        adapterObserver.setSelectedList(users);

        for (int i = 0; i < users.size(); i++) {
            observerUserList.add(users.get(i).getId());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                if (fileUri != null) {
                    String filePath = getRealPathFromURI(getContext(), fileUri);
                    Log.e("FILE_PATH", "onActivityResult: " + filePath);
                    Log.e("FILE_URI", "onActivityResult: " + fileUri);
                    if (filePath != null) {
                        File file = new File(filePath);
                        uploadFile(file);
                    }
                }
            }
        }


    }

    private void uploadFile(File file) {
        MultipartBody.Part fileToUpload = null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        try {
            fileToUpload = MultipartBody.Part.createFormData("fileUrl", URLEncoder.encode(file.getPath(), "utf-8"), requestFile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Call<ResponseSingleFile> upload = Common.getApi().uploadFileToTask(fileToUpload);
        upload.enqueue(new Callback<ResponseSingleFile>() {
            @Override
            public void onResponse
                    (@NonNull Call<ResponseSingleFile> call, @NonNull Response<ResponseSingleFile> response) {
                if (response.isSuccessful()) {
//                b.files.setText(response.body().getData().);
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleFile> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                b.progressAddFile.setVisibility(View.GONE);
                b.fileMain.setClickable(true);
            }
        });

    }

    // Declare the activity result launcher
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted, you can access the file here
                    // Perform your file access operations
                } else {
                    // Permission is denied, handle the failure
                    // Display an error message or request the permission again
                }
            });

    // Request permission at runtime
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission has already been granted, you can access the file here
            // Perform your file access operations
        } else {
            // Permission has not been granted, request it from the user
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


}