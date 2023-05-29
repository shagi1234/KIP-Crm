package tm.payhas.crm.fragment;

import static tm.payhas.crm.adapters.AdapterCloud.CLOUD_TYPE_FOLDER;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterCloud;
import tm.payhas.crm.dataModels.DataAttachment;
import tm.payhas.crm.dataModels.DataMessageTarget;
import tm.payhas.crm.dataModels.DataProjectUsers;
import tm.payhas.crm.databinding.FragmentCloudFolderBinding;
import tm.payhas.crm.interfaces.ChatRoomInterface;
import tm.payhas.crm.model.ModelFile;

public class FragmentCloudFolder extends Fragment implements ChatRoomInterface {
    private FragmentCloudFolderBinding b;
    private AdapterCloud adapterCloudFolder;
    private ArrayList<ModelFile> files = new ArrayList<>();
    private ArrayList<ModelFile> selectedArray = new ArrayList<>();

    public static FragmentCloudFolder newInstance() {
        FragmentCloudFolder fragment = new FragmentCloudFolder();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCloudFolderBinding.inflate(inflater);
        setDataArray();
        setRecycler();
        setBackground();
        initListeners();
        return b.getRoot();
    }

    private void setDataArray() {
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
        files.add(new ModelFile("Demo"));
    }

    @SuppressLint("RestrictedApi")
    private void initListeners() {
        b.deleteCommit.setOnClickListener(view -> {
            files.removeAll(selectedArray);
            adapterCloudFolder.setAll(files);
            b.deleteCommit.setVisibility(View.GONE);
            b.searchBox.setVisibility(View.VISIBLE);
            adapterCloudFolder.setSelectable(false);
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
                                Toast.makeText(getContext(), "Add", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.share:
                                Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete:
                                Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
                                adapterCloudFolder.setSelectable(true);
                                b.searchBox.setVisibility(View.GONE);
                                b.deleteCommit.setVisibility(View.VISIBLE);
                                b.deleteCount.setText("0");

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

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.main,
                0,
                50,
                0,
                0), 100);
    }

    private void setRecycler() {
        if (getActivity() != null && getContext() != null)
            adapterCloudFolder = new AdapterCloud(getContext(), getActivity(), CLOUD_TYPE_FOLDER);
        adapterCloudFolder.setAll(files);
        b.recCloudFolder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recCloudFolder.setAdapter(adapterCloudFolder);
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.searchBox, R.color.color_transparent, R.color.primary, 6, false, 1);
    }


    @Override
    public void multiSelectedArray(ArrayList<ModelFile> selected) {
        int selectedSize = selected.size();
        b.deleteCount.setText(String.valueOf(selectedSize));
        selectedArray.addAll(selected);
    }

    @Override
    public void selectedUserList(ArrayList<DataProjectUsers> selected) {

    }

    @Override
    public void userStatus(boolean isActive) {

    }

    @Override
    public void newMessage(DataMessageTarget messageTarget) {

    }

    @Override
    public void newImageImageUrl(DataAttachment attachment) {

    }

}