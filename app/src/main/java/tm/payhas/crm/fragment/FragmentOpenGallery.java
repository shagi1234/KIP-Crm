
package tm.payhas.crm.fragment;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.activity.ActivityMain;
import tm.payhas.crm.adapters.AdapterMedia;
import tm.payhas.crm.adapters.AdapterSingleChat;
import tm.payhas.crm.api.response.ResponseManyFiles;
import tm.payhas.crm.api.response.ResponseSingleFile;
import tm.payhas.crm.dataModels.MediaLocal;
import tm.payhas.crm.databinding.FragmentOpenGalleryBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.SelectedMedia;
import tm.payhas.crm.helpers.StaticMethods;
import tm.payhas.crm.interfaces.ChatRoomInterface;
import tm.payhas.crm.interfaces.OnBackPressedFragment;
import tm.payhas.crm.interfaces.UploadedFilesUrl;

public class FragmentOpenGallery extends Fragment implements OnBackPressedFragment {
    private FragmentOpenGalleryBinding b;
    private ArrayList<MediaLocal> media = new ArrayList<>();
    private String TAG = "Media";
    private AdapterMedia adapter;
    private int chooseCount;
    private int roomId;
    private AdapterSingleChat adapterSingleChat;
    private SlidrInterface slidrInterface;
    private int type;
    private int typeMessageRoom;
    public static final int SINGLE = 1;
    public static final int MANY = 2;
    private int PERMISSION_FILE = 1;

    public static FragmentOpenGallery newInstance(int chooseCount, int roomId, int type, int typeMessageRoom) {
        Bundle args = new Bundle();
        args.putInt("choose_count", chooseCount);
        args.putInt("roomId", roomId);
        args.putInt("type", type);
        args.putInt("typeMessageRoom", type);
        FragmentOpenGallery fragment = new FragmentOpenGallery();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (slidrInterface == null && getView() != null)
            slidrInterface = Slidr.replace(getView().findViewById(R.id.slider_layout), new SlidrConfig.Builder().position(SlidrPosition.LEFT).build());

        StaticMethods.setPadding(b.mainGalleryLay, 0, StaticMethods.statusBarHeight, 0, StaticMethods.navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chooseCount = getArguments().getInt("choose_count");
            roomId = getArguments().getInt("roomId");
            type = getArguments().getInt("type");
            typeMessageRoom = getArguments().getInt("typeMessageRoom");
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
        // Inflate the layout for this fragment
        b = FragmentOpenGalleryBinding.inflate(inflater, container, false);
        hideSoftKeyboard(getActivity());
        adapterSingleChat = new AdapterSingleChat(getContext(), roomId, typeMessageRoom);
        if (chooseCount == -1) {
            b.appBarLayout.setVisibility(View.VISIBLE);
        } else b.appBarLayout.setVisibility(View.GONE);

        if (checkPermissionWriteExternalStorage()) {
            AsyncTask.execute(this::getMediaFromGallery);
        }
        initListener();
        return b.getRoot();
    }

    boolean checkPermissionWriteExternalStorage() {
        if (checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PERMISSION_FILE) {
                AsyncTask.execute(this::getMediaFromGallery);
            }
        }
    }

    private void initListener() {
        b.toolbar.setVisibility(View.VISIBLE);
        b.btnBack.setOnClickListener(view -> getActivity().onBackPressed());
        StaticMethods.setBackgroundDrawable(getContext(), b.edtTitle, R.color.white, 0, 10, false, 0);
        b.btnNext.setOnClickListener(v -> {
            b.linearProgressBar.setVisibility(View.VISIBLE);
            b.main.setClickable(false);
            b.btnNext.setEnabled(false);
            SelectedMedia.getArrayList().addAll(media);
            if (type == SINGLE) {
                MediaLocal mediaLocal = SelectedMedia.getArrayList().get(0);
                uploadFile(mediaLocal);
                StaticMethods.hideSoftKeyboard(getActivity());
            } else if (type == MANY) {
                uploadManyFiles();
            }

            new Handler().postDelayed(() -> b.btnNext.setEnabled(true), 200);
        });
    }

    private void uploadFile(MediaLocal mediaLocal) {
        if (type == SINGLE) {
            MultipartBody.Part fileToUpload = null;
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"),
                            new File(mediaLocal.getPath())
                    );
            try {
                fileToUpload = MultipartBody.Part.createFormData("fileUrl", URLEncoder.encode(mediaLocal.getPath(), "utf-8"), requestFile);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            RequestBody fileUrl = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(fileToUpload));

            Call<ResponseSingleFile> upload = Common.getApi().uploadFile(fileToUpload);
            upload.enqueue(new Callback<ResponseSingleFile>() {
                @Override
                public void onResponse(@NonNull Call<ResponseSingleFile> call, @NonNull Response<ResponseSingleFile> response) {
                    if (response.isSuccessful()) {
                        Fragment chatRoom = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentChatRoom.class.getSimpleName());

                        if (chatRoom instanceof ChatRoomInterface) {
                            assert response.body() != null;
                            ((ChatRoomInterface) chatRoom).newImageImageUrl(response.body().getData());
                        }
                        if (getActivity() != null)
                            getActivity().onBackPressed();
                        b.linearProgressBar.setVisibility(View.VISIBLE);
                        b.main.setClickable(false);
                    }
                }

                @Override
                public void onFailure(Call<ResponseSingleFile> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        }

    }

    private void uploadManyFiles() {
        if (type == MANY) {
            List<MultipartBody.Part> list = new ArrayList<>();

            for (int i = 0; i < SelectedMedia.getArrayList().size(); i++) {

                MultipartBody.Part fileToUpload = null;

                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                new File(SelectedMedia.getArrayList().get(i).getPath())
                        );

                try {
                    fileToUpload = MultipartBody.Part.createFormData("fileUrl", URLEncoder.encode(SelectedMedia.getArrayList().get(i).getPath(), "utf-8"), requestFile);
                } catch (UnsupportedEncodingException e) {
                    Log.e("Add_post", "sendApi: " + e.getMessage());
                }

                list.add(fileToUpload);
            }

            Call<ResponseManyFiles> call = Common.getApi().uploadFiles(list);
            call.enqueue(new Callback<ResponseManyFiles>() {
                @Override
                public void onResponse(Call<ResponseManyFiles> call, Response<ResponseManyFiles> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "onResponse: " + response.code());
                        Fragment addNews = mainFragmentManager.findFragmentByTag(FragmentAddNews.class.getSimpleName());
                        if (addNews instanceof UploadedFilesUrl) {
                            ((UploadedFilesUrl) addNews).onUploadManyFiles(response.body().getData().getFileNames());
                        }
                        if (getActivity() != null)
                            getActivity().onBackPressed();
                        b.linearProgressBar.setVisibility(View.VISIBLE);
                        b.main.setClickable(false);
                    }
                }

                @Override
                public void onFailure(Call<ResponseManyFiles> call, Throwable t) {
                    t.getMessage();
                }
            });
        }

    }

    private void setRecycler(Cursor cursor) {
        adapter = new AdapterMedia(getActivity(), getContext(), cursor, b.laySelectionMode, b.countSelection, media, chooseCount);
        b.recGallery.setLayoutManager(new GridLayoutManager(getContext(), 3));
        b.recGallery.setAdapter(adapter);
    }

    private void getMediaFromGallery() {
        String[] columns = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE,
        };

        String selection;

        selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");

        @SuppressWarnings("deprecation")
        Cursor imagecursor = getActivity().managedQuery(queryUri,
                columns,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );
        getActivity().runOnUiThread(() -> setRecycler(imagecursor));
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    private void onNewMessage(MediaLocal mediaLocal) {

    }


}