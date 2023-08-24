package tm.payhas.crm.presentation.view.fragment;

import static android.view.Gravity.CENTER;
import static android.view.View.VISIBLE;
import static tm.payhas.crm.presentation.view.adapters.AdapterCloud.CLOUD_TYPE_FILE;
import static tm.payhas.crm.domain.helpers.FileUtil.copyFileStream;
import static tm.payhas.crm.domain.helpers.FileUtil.getPath;
import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.domain.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.domain.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.domain.helpers.StaticMethods.showToast;
import static tm.payhas.crm.domain.helpers.StaticMethods.statusBarHeight;
import static tm.payhas.crm.domain.statics.StaticConstants.APPLICATION_DIR_NAME;
import static tm.payhas.crm.domain.statics.StaticConstants.FILES_DIR;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentCloudFileBinding;
import tm.payhas.crm.presentation.view.adapters.AdapterCloud;
import tm.payhas.crm.data.remote.api.response.ResponseDataFolder;
import tm.payhas.crm.data.remote.api.response.ResponseDeleteFile;
import tm.payhas.crm.data.remote.api.response.ResponseSingleFile;
import tm.payhas.crm.domain.model.DataFolder;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.domain.helpers.FileUtil;
import tm.payhas.crm.domain.interfaces.DataFileSelectedListener;
import tm.payhas.crm.domain.interfaces.OnInternetStatus;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class FragmentCloudFile extends Fragment implements DataFileSelectedListener {
    private FragmentCloudFileBinding b;
    private AdapterCloud adapterCloudFolder;
    private String fileUrl;
    private ArrayList<DataFolder> selectedArray = new ArrayList<>();
    private AccountPreferences ac;
    private static int REQUEST_CODE = 1;
    private static int REQUEST_CODE_PERMISSION = 231;
    private Dialog dialog;

    public static FragmentCloudFile newInstance(String fileUrl) {
        FragmentCloudFile fragment = new FragmentCloudFile();
        Bundle args = new Bundle();
        args.putString("fileUrl", fileUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileUrl = getArguments().getString("fileUrl");
            Log.e("TAG", "onCreate: " + fileUrl);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentCloudFileBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        ac = new AccountPreferences(getContext());
        getFolderFiles();
        setRecycler();
        setBackground();
        initListeners();
        return b.getRoot();
    }

    private void getFolderFiles() {
        Call<ResponseDataFolder> call = Common.getApi().getFolderFiles(fileUrl);
        call.enqueue(new Callback<ResponseDataFolder>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDataFolder> call, @NonNull Response<ResponseDataFolder> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    b.swiper.setRefreshing(false);
                    b.main.setVisibility(VISIBLE);
                    b.linearProgressBar.setVisibility(View.GONE);
                    b.recCloudFile.setAlpha(1);
                    adapterCloudFolder.setAll(response.body().getData());
                    OnInternetStatus internetStatusListener = new OnInternetStatus() {};
                    internetStatusListener.setConnected(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseDataFolder> call, @NonNull Throwable t) {
                b.swiper.setRefreshing(false);
                OnInternetStatus internetStatusListener = new OnInternetStatus() {};
                internetStatusListener.setNoInternet(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.swiper,
                0,
                statusBarHeight,
                0,
                0), 100);
    }

    @SuppressLint("RestrictedApi")
    private void initListeners() {
        b.swiper.setOnRefreshListener(() -> {
            b.swiper.setRefreshing(true);
            getFolderFiles();
        });
        b.deleteCommit.setOnClickListener(view -> {
            b.deleteCommit.setEnabled(true);

            showDialog();


            new Handler().postDelayed(() -> b.deleteCommit.setEnabled(true), 200);
        });
        b.back.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        if (getContext() != null) {
            MenuBuilder menuBuilder = new MenuBuilder(getContext());
            MenuInflater menuInflater = new MenuInflater(getContext());
            menuInflater.inflate(R.menu.cloud_menu, menuBuilder);

            b.optionMenu.setOnClickListener(view -> {
                b.optionMenu.setEnabled(false);
                MenuPopupHelper popupHelper = new MenuPopupHelper(getContext(), menuBuilder, b.optionMenu);
                popupHelper.setForceShowIcon(true);
                popupHelper.show();
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.download:
                                Toast.makeText(getContext(), "Download", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.add:
                                pickFileFromInternalStorage();
                                return true;
                            case R.id.share:
                                Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete:
                                adapterCloudFolder.setSelectable(true);
                                b.searchBox.setVisibility(View.GONE);
                                b.deleteCommit.setVisibility(VISIBLE);
                                ac.setFileSelectable(true);
                                return true;
                            default:
                                return false;

                        }

                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });
                new Handler().postDelayed(() -> b.optionMenu.setEnabled(true), 200);
            });
        }
    }

    private void showDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_dialog);

        TextView done = dialog.findViewById(R.id.done_button);
        TextView cancel = dialog.findViewById(R.id.cancel_button);

        cancel.setOnClickListener(view -> dialog.dismiss());

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFile();

            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(CENTER);
    }

    private void removeFile() {
        b.linearProgressBar.setVisibility(VISIBLE);
        b.recCloudFile.setAlpha(0.5f);
        for (int i = 0; i < selectedArray.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("fileUrl", selectedArray.get(i).getFileUrl());
            Call<ResponseDeleteFile> call = Common.getApi().removeFile(jsonObject);
            call.enqueue(new Callback<ResponseDeleteFile>() {
                @Override
                public void onResponse(Call<ResponseDeleteFile> call, Response<ResponseDeleteFile> response) {
                    if (response.isSuccessful()) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseDeleteFile> call, Throwable t) {

                }
            });
        }
        selectedArray.clear();
        b.deleteCommit.setVisibility(View.GONE);
        b.searchBox.setVisibility(VISIBLE);
        adapterCloudFolder.setSelectable(false);
        ac.setFolderSelectable(false);
        dialog.dismiss();
        getFolderFiles();

    }

    private void setRecycler() {
        if (getActivity() != null && getContext() != null)
            adapterCloudFolder = new AdapterCloud(getContext(), getActivity(), CLOUD_TYPE_FILE);
        b.recCloudFile.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recCloudFile.setAdapter(adapterCloudFolder);
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.searchBox, R.color.color_transparent, R.color.primary, 6, false, 1);
    }

    @Override
    public void multiSelectedArray(ArrayList<DataFolder> selected) {
        b.deleteCount.setText(String.valueOf(selected.size()));
        selectedArray = selected;
    }

    @Override
    public void setUnSelectable() {
        adapterCloudFolder.setSelectable(false);
        b.searchBox.setVisibility(VISIBLE);
        b.deleteCommit.setVisibility(View.GONE);
        b.deleteCount.setText("0");
        selectedArray.clear();
        ac.setFileSelectable(false);
    }


    private void pickFileFromInternalStorage() {
        requestStoragePermission();
        b.linearProgressBar.setVisibility(VISIBLE);

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
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(
                                FileUtil.getMimeType(file)),
                        file);

        try {
            fileToUpload = MultipartBody.Part.createFormData("fileUrl", URLEncoder.encode(file.getPath(), "utf-8"), requestFile);
        } catch (UnsupportedEncodingException e) {
            Log.e("TAG", "uploadFile: " + e);
        }

        RequestBody directoryUrl = RequestBody.create(MediaType.parse("multipart/form-data"), fileUrl);
        RequestBody originalFileName = RequestBody.create(MediaType.parse("multipart/form-data"), file.getName());

        Call<ResponseSingleFile> upload = Common.getApi().uploadFileToCLoud(originalFileName, directoryUrl, fileToUpload);
        upload.enqueue(new Callback<ResponseSingleFile>() {
            @Override
            public void onResponse
                    (@NonNull Call<ResponseSingleFile> call, @NonNull Response<ResponseSingleFile> response) {
                b.linearProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() || response.code() == 201 || response.code() == 200) {
                    b.linearProgressBar.setVisibility(View.GONE);
                    b.main.setClickable(true);
                    getFolderFiles();
                } else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleFile> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                b.linearProgressBar.setVisibility(View.GONE);
                b.main.setClickable(true);
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