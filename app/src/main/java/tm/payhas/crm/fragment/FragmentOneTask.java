package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.normalDate;
import static tm.payhas.crm.helpers.StaticMethods.getPath;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.helpers.StaticMethods.statusBarHeight;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.activity.ActivityMain;
import tm.payhas.crm.adapters.AdapterChecklist;
import tm.payhas.crm.adapters.AdapterSelectedUsers;
import tm.payhas.crm.adapters.AdapterTaskComments;
import tm.payhas.crm.api.request.RequestTaskComment;
import tm.payhas.crm.api.response.ResponseOneTask;
import tm.payhas.crm.api.response.ResponseSingleFile;
import tm.payhas.crm.api.response.ResponseTaskComment;
import tm.payhas.crm.dataModels.DataTask;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.ChatRoomInterface;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentOneTask extends Fragment {
    private int taskId;
    private final int REQUEST_CODE = 11;
    private int projectId;
    private tm.payhas.crm.databinding.FragmentOneTaskBinding b;
    private AdapterSelectedUsers adapterObservers;
    private AdapterSelectedUsers adapterResponsible;
    private AdapterTaskComments adapterTaskComments;
    private AdapterChecklist adapterChecklist;
    private AccountPreferences ac;

    public static FragmentOneTask newInstance(int taskId) {
        FragmentOneTask fragment = new FragmentOneTask();
        Bundle args = new Bundle();
        args.putInt("task_id", taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getInt("task_id");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.main,
                0,
                statusBarHeight,
                0,
                0), 50);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = tm.payhas.crm.databinding.FragmentOneTaskBinding.inflate(inflater);
        ac = new AccountPreferences(getContext());
        initListeners();
        setRecycler();
        getTaskInfo();
        return b.getRoot();
    }

    private void setRecycler() {
        adapterObservers = new AdapterSelectedUsers(getContext(), 1);
        b.rvObservers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.rvObservers.setAdapter(adapterObservers);

        adapterResponsible = new AdapterSelectedUsers(getContext(), 1);
        b.rvResponsibles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.rvResponsibles.setAdapter(adapterResponsible);

        adapterTaskComments = new AdapterTaskComments(getContext());
        b.recyclerComments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        b.recyclerComments.setAdapter(adapterTaskComments);

        adapterChecklist = new AdapterChecklist(getContext());
        b.recyclerCheckpoint.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recyclerCheckpoint.setAdapter(adapterChecklist);
    }

    private void initListeners() {
        b.back.setOnClickListener(view -> getActivity().onBackPressed());
        b.cancelTaskClicker.setOnClickListener(view -> changeTaskStatus());
        b.btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFileFromInternalStorage();
            }
        });
        b.btnSendComment.setOnClickListener(view -> {
            sendComment();
            b.infoInput.setText("");
        });
        b.addChecklistClicker.setOnClickListener(view -> addFragment(mainFragmentManager, R.id.main_content, FragmentAddChecklist.newInstance(taskId, projectId)));
    }

    private void pickFileFromInternalStorage() {
        String[] mimeTypes = {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                "text/plain", "application/pdf", "application/zip"};

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);


        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                if (fileUri != null) {
                    String filePath = getPath(getContext(), fileUri);
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
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        new File(file.getPath())
                );
        try {
            fileToUpload = MultipartBody.Part.createFormData("fileUrl", URLEncoder.encode(file.getPath(), "utf-8"), requestFile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestBody fileUrl = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(fileToUpload));

        Call<ResponseSingleFile> upload = Common.getApi().uploadFileToTask(fileToUpload);
        upload.enqueue(new Callback<ResponseSingleFile>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSingleFile> call, @NonNull Response<ResponseSingleFile> response) {
                if (response.isSuccessful()) {
                    Fragment chatRoom = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentChatRoom.class.getSimpleName());

                    if (chatRoom instanceof ChatRoomInterface) {
                        assert response.body() != null;
                        ((ChatRoomInterface) chatRoom).newImageImageUrl(response.body().getData());
                    }
                    sendComment();
                    b.main.setClickable(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleFile> call, Throwable t) {
                Log.e("OneTask", "onFailure: " + t.getMessage());
            }
        });
    }


    private void changeTaskStatus() {
        Call<ResponseOneTask> call = Common.getApi().changeTaskStatus(ac.getToken(), taskId);
        call.enqueue(new Callback<ResponseOneTask>() {
            @Override
            public void onResponse(Call<ResponseOneTask> call, Response<ResponseOneTask> response) {
                if (response.isSuccessful()) {
                    getTaskInfo();
                }
            }

            @Override
            public void onFailure(Call<ResponseOneTask> call, Throwable t) {

            }
        });
    }

    private void sendComment() {
        RequestTaskComment requestTaskComment = new RequestTaskComment();
        requestTaskComment.setText(b.infoInput.getText().toString());
        requestTaskComment.setTaskId(taskId);
        Call<ResponseTaskComment> call = Common.getApi().createComment(ac.getToken(), requestTaskComment);
        call.enqueue(new Callback<ResponseTaskComment>() {
            @Override
            public void onResponse(Call<ResponseTaskComment> call, Response<ResponseTaskComment> response) {
                if (response.isSuccessful()) {
                    adapterTaskComments.addComment(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseTaskComment> call, Throwable t) {

            }
        });
    }

    private void getTaskInfo() {
        Call<ResponseOneTask> call = Common.getApi().getOneTask(ac.getToken(), taskId);
        call.enqueue(new Callback<ResponseOneTask>() {
            @Override
            public void onResponse(Call<ResponseOneTask> call, Response<ResponseOneTask> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData().isAuthor() || response.body().getData().isExecutor()) {
                        b.cancelTaskClicker.setVisibility(View.VISIBLE);
                    }
                    setInfo(response.body().getData());

                    adapterObservers.setUserList(response.body().getData().getObserverUsers());
                    adapterResponsible.setUserList(response.body().getData().getResponsibleUsers());
                    adapterTaskComments.setComments(response.body().getData().getComments());
                    adapterChecklist.setChecklists(response.body().getData().getChecklists());
                    Log.e("ChecklistSizeReceived", "onResponse: " + response.body().getData().getChecklists().size());
                    projectId = response.body().getData().getProjectId();
                }
            }

            @Override
            public void onFailure(Call<ResponseOneTask> call, Throwable t) {

            }
        });
    }

    private void setInfo(DataTask data) {
        b.taskEndTime.setText(normalDate(data.getFinishesAt()));
        b.taskTimeStart.setText(normalDate(data.getStartsAt()));
        b.taskStatus.setText(data.getStatus());
        b.taskRemainingDays.setText(String.valueOf(data.getTimeOut()));
    }
}