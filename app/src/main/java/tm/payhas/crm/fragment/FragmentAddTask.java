package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.fragment.FragmentSpinner.OBSERVERS;
import static tm.payhas.crm.fragment.FragmentSpinner.PROJECTS;
import static tm.payhas.crm.fragment.FragmentSpinner.PROJECT_EXECUTOR;
import static tm.payhas.crm.fragment.FragmentSpinner.RESPONSIBLE;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.normalDate;
import static tm.payhas.crm.helpers.Common.normalTime;
import static tm.payhas.crm.helpers.FileUtil.copyFileStream;
import static tm.payhas.crm.helpers.FileUtil.getPath;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.helpers.StaticMethods.showToast;
import static tm.payhas.crm.statics.StaticConstants.APPLICATION_DIR_NAME;
import static tm.payhas.crm.statics.StaticConstants.FILES_DIR;
import static tm.payhas.crm.statics.StaticConstants.FINISHED;
import static tm.payhas.crm.statics.StaticConstants.HIGH;
import static tm.payhas.crm.statics.StaticConstants.IN_PROCESS;
import static tm.payhas.crm.statics.StaticConstants.MEDIUM;
import static tm.payhas.crm.statics.StaticConstants.NOT_IMPORTANT;
import static tm.payhas.crm.statics.StaticConstants.NOT_STARTED;
import static tm.payhas.crm.statics.StaticConstants.PRIMARY;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

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
import tm.payhas.crm.api.response.ResponseOneTask;
import tm.payhas.crm.api.response.ResponseSingleFile;
import tm.payhas.crm.api.response.ResponseTasks;
import tm.payhas.crm.dataModels.DataAttachment;
import tm.payhas.crm.dataModels.DataFile;
import tm.payhas.crm.dataModels.DataProject;
import tm.payhas.crm.dataModels.DataTask;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.FileUtil;
import tm.payhas.crm.interfaces.AddTask;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentAddTask extends Fragment implements AddTask {
    private tm.payhas.crm.databinding.FragmentAddTaskBinding b;
    private static final int REQUEST_CODE = 112211;
    private AccountPreferences accountPreferences;
    private String importancyToSend;
    private int projectId = 0;
    private int executorId;
    private ArrayList<Integer> responsibleUsersList = new ArrayList<>();
    private ArrayList<Integer> observerUserList = new ArrayList<>();
    private String reminderTypeToSend = "";
    private String taskStatusToSend = "";
    private String timeEnd = "";
    private String timeStart = "";
    private String timeRemind = "";
    private AdapterSelectedUsers adapterObserver;
    private AdapterSelectedUsers adapterResponsible;
    private DataFile fileTo;
    private DataAttachment fileLoaded = null;
    private int taskId;
    private ArrayAdapter<String> adapterImportancy;
    private ArrayAdapter<String> adapterReminderType;
    private ArrayAdapter<String> adapterStatus;

    public static FragmentAddTask newInstance(int projectId, int taskId) {
        FragmentAddTask fragment = new FragmentAddTask();
        Bundle args = new Bundle();
        args.putInt("projectId", projectId);
        args.putInt("taskId", taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getInt("projectId");
            taskId = getArguments().getInt("taskId");
        }
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = tm.payhas.crm.databinding.FragmentAddTaskBinding.inflate(inflater);
        accountPreferences = new AccountPreferences(getContext());
        hideSoftKeyboard(getActivity());
        setProjectSelectable();
        setUpSpinners();
        initListeners();
        setRecyclers();
        setInfo();
        buttonActivity();
        return b.getRoot();
    }

    private void buttonActivity() {
        if (b.edtTaskName.getText().toString().length() > 0 && b.infoInput.getText().toString().length() > 0 && !Objects.equals(reminderTypeToSend, "") && !Objects.equals(timeEnd, "") && !Objects.equals(timeStart, "") && adapterObserver.getSelectedList().size() > 0 && adapterResponsible.getSelectedList().size() > 0 && executorId != 0) {
            b.btnSave.setEnabled(true);
            setBackgroundDrawable(getContext(), b.btnSaveText, R.color.primary, 0, 10, false, 0);
        } else {
            b.btnSave.setEnabled(false);
            setBackgroundDrawable(getContext(), b.btnSaveText, R.color.primary_30, 0, 10, false, 0);

        }
    }

    private void setInfo() {
        if (taskId != 0) {
            Call<ResponseOneTask> call = Common.getApi().getOneTask(accountPreferences.getToken(), taskId);
            call.enqueue(new Callback<ResponseOneTask>() {
                @Override
                public void onResponse(Call<ResponseOneTask> call, Response<ResponseOneTask> response) {
                    if (response.isSuccessful()) {
                        setInfo(response.body().getData());
                        setProjectUsers(response.body().getData());
                        setFile(response.body().getData());
                        Log.e("ChecklistSizeReceived", "onResponse: " + response.body().getData().getChecklists().size());
                    }
                }

                @Override
                public void onFailure(Call<ResponseOneTask> call, Throwable t) {

                }
            });
        }
    }

    private void setFile(DataTask data) {
        fileLoaded = data.getFiles().get(0);
    }

    private void setProjectUsers(DataTask data) {
        ArrayList<DtoUserInfo> selectedResponsible = new ArrayList<>();
        ArrayList<DtoUserInfo> selectedObservers = new ArrayList<>();
        ArrayList<DataProject.UserInTask> usersResponsible = data.getResponsibleUsers();
        ArrayList<DataProject.UserInTask> usersObservers = data.getResponsibleUsers();
        for (int i = 0; i < data.getResponsibleUsers().size(); i++) {
            selectedResponsible.add(usersResponsible.get(i).getUser());
        }
        adapterResponsible.setSelectedList(selectedResponsible);
        for (int i = 0; i < selectedResponsible.size(); i++) {
            responsibleUsersList.add(selectedResponsible.get(i).getId());
        }
        for (int i = 0; i < data.getObserverUsers().size(); i++) {
            selectedObservers.add(usersObservers.get(i).getUser());
        }
        adapterObserver.setSelectedList(selectedObservers);
        for (int i = 0; i < selectedObservers.size(); i++) {
            observerUserList.add(selectedObservers.get(i).getId());
        }
    }

    private void setInfo(DataTask data) {
        b.taskExecutor.setText(String.format("%s %s", data.getExecutor().getPersonalData().getName(), data.getExecutor().getPersonalData().getLastName()));
        if (projectId != 0 || data.getProject() != null)
            b.vTaskProjects.setText(data.getProject().getName());
        b.edtTaskName.setText(data.getName());
        b.infoInput.setText(data.getDescription());
        b.files.setText(data.getFiles().get(0).getFileName());
        b.timeEnd.setText(normalDate(data.getFinishesAt()));
        b.timeStart.setText(normalDate(data.getStartsAt()));
        executorId = data.getExecutorId();
        taskId = data.getId();
        timeRemind = data.getRemindAt().get(0);
        timeStart = data.getStartsAt();
        timeEnd = data.getFinishesAt();
        if (data.getRemindAt() != null || data.getRemindAt().size() != 0) {
            b.timeReminder.setText(String.format("%s %s", normalDate(data.getRemindAt().get(0)), normalTime(data.getRemindAt().get(0))));

        }
        setSpinnerSelectedReminderType(data.getReminderType());
        setSpinnerSelectedImportant(data.getPriority());
    }

    private void setSpinnerSelectedImportant(@NonNull String status) {
        String selectedValue;
        Log.e("Importancy", "setSpinnerSelectedReminderImportancy:" + status);
        switch (status) {
            case PRIMARY:
                importancyToSend = PRIMARY;
                selectedValue = "Низкий";
                setSpinnerSelected(adapterImportancy, b.spinnerTaskImportancy, selectedValue);
                break;
            case MEDIUM:
                importancyToSend = MEDIUM;
                selectedValue = "Средний";
                setSpinnerSelected(adapterImportancy, b.spinnerTaskImportancy, selectedValue);
                break;
            case HIGH:
                importancyToSend = HIGH;
                selectedValue = "Высокий";
                setSpinnerSelected(adapterImportancy, b.spinnerTaskImportancy, selectedValue);
                break;
            case NOT_IMPORTANT:
                importancyToSend = NOT_IMPORTANT;
                selectedValue = "Неотложный";
                setSpinnerSelected(adapterImportancy, b.spinnerTaskImportancy, selectedValue);
                break;
        }
    }

    private void setSpinnerSelectedReminderType(String reminderType) {
        String toSend;
        String selectionValue;
        switch (reminderType) {

            case "author":
                selectionValue = "Автор";
                toSend = "author";
                reminderTypeToSend = toSend;
                setSpinnerSelected(adapterReminderType, b.spinnerReminderType, selectionValue);
                break;
            case "observerUsers":
                selectionValue = "Наблюдатели";
                toSend = "observerUsers";
                reminderTypeToSend = toSend;
                setSpinnerSelected(adapterReminderType, b.spinnerReminderType, selectionValue);
                break;
            case "responsibleUsers":
                selectionValue = "Ответственные";
                toSend = "responsibleUsers";
                reminderTypeToSend = toSend;
                setSpinnerSelected(adapterReminderType, b.spinnerReminderType, selectionValue);
                break;

        }
    }

    public void setSpinnerSelected(ArrayAdapter adapter, Spinner spinner, String selectedValue) {
        adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(selectedValue);
            if (position != -1) {
                spinner.setSelection(position);
            }
        }
    }

    private void setProjectSelectable() {
        if (projectId != 0) {
            b.layProjects.setVisibility(View.GONE);
        }
    }

    private void setRecyclers() {
        adapterObserver = new AdapterSelectedUsers(getContext(), 2);
        b.taskObservers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.taskObservers.setAdapter(adapterObserver);

        adapterResponsible = new AdapterSelectedUsers(getContext(), 2);
        b.taskResponseible.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.taskResponseible.setAdapter(adapterResponsible);
    }

    private void setUpSpinners() {
        b.spinnerTaskStatus.setVisibility(View.GONE);
        String[] statuses = getResources().getStringArray(R.array.status);
        String[] importancy = getResources().getStringArray(R.array.importancy);
        String[] reminderType = getResources().getStringArray(R.array.reminder_type);
        adapterReminderType = new ArrayAdapter<>(getContext(), R.layout.item_spinner, reminderType);
        adapterStatus = new ArrayAdapter<>(getContext(), R.layout.item_spinner, statuses);
        adapterImportancy = new ArrayAdapter<>(getContext(), R.layout.item_spinner, importancy);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterReminderType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterImportancy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnerTaskImportancy.setAdapter(adapterImportancy);
        b.spinnerReminderType.setAdapter(adapterReminderType);
        b.spinnerTaskStatus.setAdapter(adapterStatus);
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
        b.files.setOnClickListener(view -> pickFileFromInternalStorage());
        b.timeEnd.setOnClickListener(view -> openDialog(b.timeEnd, 1));
        b.timeReminder.setOnClickListener(view -> openDialog(b.timeReminder, 2));
        b.timeStart.setOnClickListener(view -> openDialog(b.timeStart, 3));
        b.vTaskProjects.setOnClickListener(view -> {
            b.vTaskProjects.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(PROJECTS, projectId, 0, null));
            new Handler().postDelayed(() -> b.vTaskProjects.setEnabled(true), 200);
        });
        b.editObservers.setOnClickListener(view -> {
            b.editObservers.setEnabled(false);
            observerUserList = adapterObserver.getSelectedList();
            Log.e("Selected Observer", "initListeners: " + adapterObserver.getSelectedList().size());
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(OBSERVERS, projectId, 0, observerUserList));
            new Handler().postDelayed(() -> b.editObservers.setEnabled(true), 200);

        });
        b.taskExecutor.setOnClickListener(view -> {
            b.taskExecutor.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(PROJECT_EXECUTOR, projectId, 0, null));
            new Handler().postDelayed(() -> b.taskExecutor.setEnabled(true), 200);
        });
        b.editResponsible.setOnClickListener(view -> {
            b.editResponsible.setEnabled(false);
            responsibleUsersList = adapterResponsible.getSelectedList();
            Log.e("Selected Responsible", "initListeners: " + adapterResponsible.getSelectedList().size());
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(RESPONSIBLE, projectId, 0, responsibleUsersList));
            new Handler().postDelayed(() -> b.editResponsible.setEnabled(true), 200);
        });
        b.spinnerReminderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                String toSend;
                switch (selected) {
                    case "Автор":
                        toSend = "author";
                        reminderTypeToSend = toSend;
                        break;
                    case "Наблюдатели":
                        toSend = "observerUsers";
                        reminderTypeToSend = toSend;
                        break;
                    case "Ответственные":
                        toSend = "responsibleUsers";
                        reminderTypeToSend = toSend;
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
                        taskStatusToSend = toSend;
                        break;
                    case "В процессе":
                        toSend = IN_PROCESS;
                        taskStatusToSend = toSend;
                        break;
                    case "Завершенo":
                        toSend = FINISHED;
                        taskStatusToSend = toSend;
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
                        importancyToSend = toSend;
                        break;
                    case "Средний":
                        toSend = MEDIUM;
                        importancyToSend = toSend;
                        break;
                    case "Высокий":
                        toSend = HIGH;
                        importancyToSend = toSend;
                        break;
                    case "Неотложный":
                        toSend = NOT_IMPORTANT;
                        importancyToSend = toSend;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        b.btnSave.setOnClickListener(view -> createTask());
        b.infoInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonActivity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        b.edtTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonActivity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void openDialog(TextView dateSet, int type) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog;
        if (type == 2) {
            // Open Combined DatePickerDialog and TimePickerDialog
            datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
                c.set(Calendar.YEAR, year1);
                c.set(Calendar.MONTH, month1);
                c.set(Calendar.DAY_OF_MONTH, day1);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hourOfDay, minute) -> {
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    c.set(Calendar.MINUTE, minute);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    timeRemind = df.format(c.getTime());
                    buttonActivity();
                    dateSet.setText(String.valueOf(day1) + "/" + String.valueOf(month1 + 1) + "/" + String.valueOf(year1) + " " + String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                timePickerDialog.show();

            }, year, month, day);
        } else {
            // Regular DatePickerDialog for types 1 and 3
            datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
                c.set(Calendar.YEAR, year1);
                c.set(Calendar.MONTH, month1);
                c.set(Calendar.DAY_OF_MONTH, day1);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

                if (type == 1) {
                    timeEnd = df.format(c.getTime());
                    buttonActivity();
                } else if (type == 3) {
                    timeStart = df.format(c.getTime());
                    buttonActivity();
                }

                dateSet.setText(String.valueOf(day1) + "/" + String.valueOf(month1 + 1) + "/" + String.valueOf(year1));
            }, year, month, day);
        }
        datePickerDialog.show();
    }

    private void createTask() {
        ArrayList<String> remindAt = new ArrayList<>();
        remindAt.add(timeRemind);
        RequestCreateTask requestCreateTask = new RequestCreateTask();
        requestCreateTask.setName(b.edtTaskName.getText().toString());
        requestCreateTask.setProjectId(projectId);
        requestCreateTask.setPriority(importancyToSend);
        requestCreateTask.setExecutorId(executorId);
        requestCreateTask.setRemindAt(remindAt);
        requestCreateTask.setStartsAt(timeStart);
        requestCreateTask.setFinishesAt(timeEnd);
        requestCreateTask.setResponsibleUsers(responsibleUsersList);
        requestCreateTask.setObserverUsers(observerUserList);
        requestCreateTask.setDescription(b.infoInput.getText().toString());
        requestCreateTask.setReminderType(reminderTypeToSend);
        requestCreateTask.setId(taskId);
        ArrayList<DataAttachment> files = new ArrayList<>();
        if (fileTo != null) {
            DataAttachment attachment = new DataAttachment();
            attachment.setFileUrl(fileTo.getUrl());
            attachment.setFileName(fileTo.getOriginalFileName());
            files.add(attachment);
        } else if (fileLoaded != null) {
            files.add(fileLoaded);
        }
        requestCreateTask.setFiles(files);

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

    private void uploadFile(File file) {
        MultipartBody.Part fileToUpload = null;
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(FileUtil.getMimeType(file)),
                file);
        try {
            fileToUpload = MultipartBody.Part.createFormData("fileUrl", URLEncoder.encode(file.getPath(), "utf-8"), requestFile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestBody originalFileName = RequestBody.create(MediaType.parse("multipart/form-data"), file.getName());

        Call<ResponseSingleFile> upload = Common.getApi().uploadFileToTask(originalFileName, fileToUpload);
        upload.enqueue(new Callback<ResponseSingleFile>() {
            @Override
            public void onResponse
                    (@NonNull Call<ResponseSingleFile> call, @NonNull Response<ResponseSingleFile> response) {
                if (response.isSuccessful()) {
                    b.progressAddFile.setVisibility(View.GONE);
                    b.fileMain.setVisibility(View.VISIBLE);
                    b.fileMain.setClickable(true);
                    b.files.setText(response.body().getData().getOriginalFileName());
                    fileTo = response.body().getData();
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleFile> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                b.progressAddFile.setVisibility(View.GONE);
                b.fileMain.setClickable(true);
                b.fileMain.setVisibility(View.VISIBLE);

            }
        });

    }

    public void getFile(Intent data) {
        if (getActivity() == null) return;
        String filename;
        String fullFilePath = "";
        File resultFile = null;
        int fileSize = 0;

        try {
            Uri uri = data.getData();

            String mimeType = getActivity().getContentResolver().getType(uri);

            if (mimeType == null) {
                String path = getPath(getContext(), uri);

                if (path == null) {
                    filename = FilenameUtils.getName(uri.toString());
                } else {
                    File file = new File(path);
                    filename = file.getName();
                }
            } else {
                Uri returnUri = data.getData();

                Cursor returnCursor = getActivity().getContentResolver().query(returnUri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);

                String size = Long.toString(returnCursor.getLong(sizeIndex));
                fileSize = Integer.parseInt(size);


            }
            String sourcePath = Environment.getExternalStorageDirectory() + File.separator + APPLICATION_DIR_NAME + FILES_DIR;


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                getActivity().getApplicationContext().getExternalFilesDir("Salam/Files/");
                resultFile = new File(getActivity().getApplicationContext().getExternalFilesDir("Salam/Files/"), filename);
                copyFileStream(resultFile, uri, getContext());
                fullFilePath = resultFile.getPath();

            } else {
                try {
                    resultFile = new File(sourcePath + filename);
                    fullFilePath = sourcePath + filename;
                    copyFileStream(resultFile, uri, getContext());

                } catch (Exception e) {
                    Log.e("error", "onActivityResult: " + e.getMessage());
                }
            }


            if (resultFile != null && resultFile.exists()) {

                int finalFileSize = fileSize;
                String finalFullFilePath = fullFilePath;
                File finalResultFile = resultFile;
                uploadFile(finalResultFile);


            } else {
                showToast(getActivity(), "Not found");
            }

        } catch (Exception e) {
            Log.e("TAG_error", "onActivityResult: " + e.getMessage());
        }
    }

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

    @Override
    public void selectedProjectId(DataProject oneProject) {
        projectId = oneProject.getId();
        accountPreferences.setPrefCurrentProjectId(oneProject.getId());
        b.vTaskProjects.setText(oneProject.getName());
        buttonActivity();
    }

    @Override
    public void setExecutor(DtoUserInfo user) {
        b.taskExecutor.setText(String.format("%s %s" ,user.getPersonalData().getName(), user.getPersonalData().getSurname()));
        executorId = user.getId();
        buttonActivity();
    }

    @Override
    public void getResponsibleUsers(ArrayList<DtoUserInfo> users) {
        Log.e("ADD_TASK", "getResponsibleUsers: " + users.size());
        adapterResponsible.setSelectedList(users);

        for (int i = 0; i < users.size(); i++) {
            responsibleUsersList.add(users.get(i).getId());
        }
        buttonActivity();
    }

    @Override
    public void getObserverUsers(ArrayList<DtoUserInfo> users) {
        Log.e("ADD_TASK", "getObserverUsers: " + users.size());
        adapterObserver.setSelectedList(users);

        for (int i = 0; i < users.size(); i++) {
            observerUserList.add(users.get(i).getId());
        }
        buttonActivity();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                getFile(data);
            }
        }
    }

}