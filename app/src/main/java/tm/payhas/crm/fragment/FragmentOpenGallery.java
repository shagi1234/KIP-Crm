
package tm.payhas.crm.fragment;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.activity.ActivityMain;
import tm.payhas.crm.adapters.AdapterMedia;
import tm.payhas.crm.adapters.AdapterSingleChat;
import tm.payhas.crm.dataModels.DataAttachment;
import tm.payhas.crm.dataModels.MediaLocal;
import tm.payhas.crm.databinding.FragmentOpenGalleryBinding;
import tm.payhas.crm.helpers.SelectedMedia;
import tm.payhas.crm.helpers.StaticMethods;
import tm.payhas.crm.interfaces.ChatRoomInterface;
import tm.payhas.crm.interfaces.OnBackPressedFragment;

public class FragmentOpenGallery extends Fragment implements OnBackPressedFragment {
    private FragmentOpenGalleryBinding b;
    private ArrayList<MediaLocal> media = new ArrayList<>();
    private String TAG = "Media";
    private AdapterMedia adapter;
    private int chooseCount;
    private AdapterSingleChat adapterSingleChat;
    private SlidrInterface slidrInterface;
    private int PERMISSION_FILE = 1;

    public static FragmentOpenGallery newInstance(int chooseCount) {
        Bundle args = new Bundle();
        args.putInt("choose_count", chooseCount);
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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentOpenGalleryBinding.inflate(inflater, container, false);
        adapterSingleChat = new AdapterSingleChat(getContext());
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
            b.btnNext.setEnabled(false);
            new Handler().postDelayed(() -> b.btnNext.setEnabled(true), 200);
            DataAttachment attachment = new DataAttachment();
            MediaLocal mediaLocal = SelectedMedia.getArrayList().get(0);
            attachment.setFileUrl(mediaLocal.getPath());
            attachment.setFileName("");
            onNewMessage(attachment);
            StaticMethods.hideSoftKeyboard(getActivity());
            getActivity().onBackPressed();
        });
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

    private void onNewMessage(DataAttachment attachment) {
        Fragment fragment = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentChatRoom.class.getSimpleName());
        if (fragment instanceof ChatRoomInterface) {
            ((ChatRoomInterface) fragment).newImageImageUrl(attachment);
        }
    }
}