package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.normalDate;
import static tm.payhas.crm.helpers.FileUtil.copyFileStream;
import static tm.payhas.crm.helpers.FileUtil.getPath;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.helpers.StaticMethods.showToast;
import static tm.payhas.crm.helpers.StaticMethods.statusBarHeight;
import static tm.payhas.crm.statics.StaticConstants.APPLICATION_DIR_NAME;
import static tm.payhas.crm.statics.StaticConstants.FILES_DIR;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterSelectedUsers;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.api.request.RequestNewProject;
import tm.payhas.crm.api.response.ResponseOneProject;
import tm.payhas.crm.api.response.ResponseSingleFile;
import tm.payhas.crm.dataModels.DataAttachment;
import tm.payhas.crm.dataModels.DataFile;
import tm.payhas.crm.dataModels.DataProject;
import tm.payhas.crm.databinding.FragmentAddProjectBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.FileUtil;
import tm.payhas.crm.interfaces.HelperAddProject;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentAddProject extends Fragment implements HelperAddProject {
    private static final int REQUEST_CODE = 1122;
    private FragmentAddProjectBinding b;
    private AdapterSelectedUsers adapterSelectedUsers;
    private AdapterSelectedUsers adapterExecutor;
    private ArrayList<Integer> selectedUserList = new ArrayList<>();
    private String timeStart;
    private String timeEnd;
    private int executorId;
    private boolean toEdit;
    private int projectId;
    private DataFile fileUploaded = null;
    private DataAttachment fileLoaded = null;
    // Declare the activity result launcher
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted, you can access the file here
                    // Perform your file access operations
                } else {
                    // Permission is denied, handle the failure
                    // Display an error message or request the permission again
                }
            });

    public static FragmentAddProject newInstance(boolean toEdit, int projectId) {
        FragmentAddProject fragment = new FragmentAddProject();
        Bundle args = new Bundle();
        args.putBoolean("editable", toEdit);
        args.putInt("projectId", projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            toEdit = getArguments().getBoolean("editable");
            projectId = getArguments().getInt("projectId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentAddProjectBinding.inflate(inflater);
        setRecycler();
        loadToEdit();
        initListeners();
        return b.getRoot();
    }

    private void loadToEdit() {
        if (toEdit && projectId != 0) {
            Call<ResponseOneProject> call = Common.getApi().getOneProject(AccountPreferences.newInstance(getContext()).getToken(), projectId, 1, 1);
            call.enqueue(new Callback<ResponseOneProject>() {
                @Override
                public void onResponse(Call<ResponseOneProject> call, Response<ResponseOneProject> response) {
                    if (response.isSuccessful()) {
                        b.main.setVisibility(View.VISIBLE);
                        setProjectInfo(response.body().getData());
                        setProjectUsers(response.body().getData());
                        setProjectExecutor(response.body().getData());
                        setFile(response.body().getData());

                    }
                }

                @Override
                public void onFailure(Call<ResponseOneProject> call, Throwable t) {
                }
            });
        }
    }

    private void setFile(DataProject data) {
        fileLoaded = data.getFile();
    }

    private void setProjectExecutor(DataProject data) {
        ArrayList<DtoUserInfo> selectedList = new ArrayList<>();
        DtoUserInfo userInfo = data.getExecutor();
        selectedList.add(userInfo);
        adapterExecutor.setSelectedList(selectedList);
    }

    private void setProjectUsers(DataProject data) {
        ArrayList<DtoUserInfo> selectedList = new ArrayList<>();
        ArrayList<DataProject.UserInTask> usersList = data.getProjectParticipants();
        for (int i = 0; i < data.getProjectParticipants().size(); i++) {
            selectedList.add(usersList.get(i).getUser());
        }
        adapterSelectedUsers.setSelectedList(selectedList);
        for (int i = 0; i < selectedList.size(); i++) {
            selectedUserList.add(selectedList.get(i).getId());
        }
    }

    private void setProjectInfo(DataProject data) {
        projectId = data.getId();
        b.edtNameProject.setText(data.getName());
        b.descriptionProject.setText(data.getDescription());
        b.attachFileProject.setText(data.getFile().getFileName());
        b.projectEndTime.setText(normalDate(data.getDeadline()));
        b.projectStartTime.setText(normalDate(data.getStartsAt()));
        timeEnd = data.getDeadline();
        timeStart = data.getStartsAt();
        executorId = data.getExecutorId();
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

    private void addNewProject() {
        RequestNewProject request = new RequestNewProject();
        request.setName(b.edtNameProject.getText().toString());
        request.setDescription(b.descriptionProject.getText().toString());
        request.setDeadline(timeEnd);
        request.setStartsAt(timeStart);
        request.setProjectParticipants(selectedUserList);
        request.setExecutorId(executorId);
        request.setId(projectId);
        if (fileLoaded!=null){
            request.setFile(fileLoaded);
        }

        DataAttachment attachment = new DataAttachment();
        if (fileUploaded != null) {
            attachment.setFileName(fileUploaded.getOriginalFileName());
            attachment.setFileUrl(fileUploaded.getUrl());
        }
        request.setFile(attachment);

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
        adapterSelectedUsers = new AdapterSelectedUsers(getContext(), 2);
        b.membersProject.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.membersProject.setAdapter(adapterSelectedUsers);
        adapterExecutor = new AdapterSelectedUsers(getContext(), 2);
        b.executorProject.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.executorProject.setAdapter(adapterExecutor);
    }

    private void initListeners() {
        b.attachFileProject.setOnClickListener(view -> pickFileFromInternalStorage());
        b.btnSave.setOnClickListener(view -> {
            b.btnSave.setEnabled(false);
            addNewProject();
            new Handler().postDelayed(() -> b.btnSave.setEnabled(true), 200);
        });
        b.editMembers.setOnClickListener(view -> {
            b.editMembers.setEnabled(false);
            selectedUserList = adapterSelectedUsers.getSelectedList();
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(FragmentSpinner.PROJECT_MEMBERS, 0,0, selectedUserList));
            new Handler().postDelayed(() -> b.editMembers.setEnabled(true), 200);
        });
        b.editExecutor.setOnClickListener(view -> {
            b.editExecutor.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentSpinner.newInstance(FragmentSpinner.PROJECT_EXECUTOR, 0,0, null));
            new Handler().postDelayed(() -> b.editExecutor.setEnabled(true), 200);
        });
        b.projectStartTime.setOnClickListener(view -> openDialog(b.projectStartTime));
        b.projectEndTime.setOnClickListener(view -> openDialog2(b.projectEndTime));

    }

    private void pickFileFromInternalStorage() {
        requestStoragePermission();
        b.attachFileProject.setVisibility(View.GONE);
        b.progressFile.setVisibility(View.VISIBLE);

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

    private void openDialog(TextView dateSet) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> dateSet.setText(day1 + "/" + (month1 + 1) + "/" + year1), year, month, day);
        datePickerDialog.show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        timeStart = df.format(c.getTime());
    }

    private void openDialog2(TextView dateSet) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> dateSet.setText(day1 + "/" + (month1 + 1) + "/" + year1), year, month, day);
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
            selectedUserList.add(userSelectedList.get(i).getId());
        }
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
                    b.progressFile.setVisibility(View.GONE);
                    b.attachFileProject.setVisibility(View.VISIBLE);
                    b.attachFileProject.setText(response.body().getData().getOriginalFileName());
                    fileUploaded = response.body().getData();
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleFile> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                b.progressFile.setVisibility(View.GONE);
                b.attachFileProject.setClickable(true);
            }
        });

    }

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