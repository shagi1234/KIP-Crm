package tm.payhas.crm.fragment;

import static tm.payhas.crm.adapters.AdapterCloud.CLOUD_TYPE_FOLDER;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
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

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterCloud;
import tm.payhas.crm.api.response.ResponseDataFolder;
import tm.payhas.crm.api.response.ResponseDeleteFile;
import tm.payhas.crm.dataModels.DataFolder;
import tm.payhas.crm.databinding.FragmentCloudFolderBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.DataFileSelectedListener;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentCloudFolder extends Fragment implements DataFileSelectedListener {
    private FragmentCloudFolderBinding b;
    private AdapterCloud adapterCloudFolder;
    private ArrayList<DataFolder> selectedArray = new ArrayList<>();
    private AccountPreferences ac;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCloudFolderBinding.inflate(inflater);
        setHelpers();
        hideSoftKeyboard(getActivity());
        getCloudFolders();
        setRecycler();
        setBackground();
        initListeners();
        return b.getRoot();
    }

    private void setHelpers() {
        ac = new AccountPreferences(getContext());
    }

    private void getCloudFolders() {
        Call<ResponseDataFolder> call = Common.getApi().getCloudFolder();
        call.enqueue(new Callback<ResponseDataFolder>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDataFolder> call, @NonNull Response<ResponseDataFolder> response) {
                if (response.isSuccessful()) {
                    b.swiper.setRefreshing(false);
                    b.linearProgressBar.setVisibility(View.GONE);
                    b.recCloudFolder.setAlpha(1);
                    assert response.body() != null;
                    if (response.body().getData() != null)
                        adapterCloudFolder.setAll(response.body().getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseDataFolder> call, @NonNull Throwable t) {
                b.linearProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }


    @SuppressLint("RestrictedApi")
    private void initListeners() {
        b.swiper.setOnRefreshListener(this::getCloudFolders);
        b.deleteCommit.setOnClickListener(view -> {
            b.linearProgressBar.setVisibility(View.VISIBLE);
            b.recCloudFolder.setAlpha(0.5f);
            removeFile();
            getCloudFolders();
            b.deleteCommit.setVisibility(View.GONE);
            b.searchBox.setVisibility(View.VISIBLE);
            adapterCloudFolder.setSelectable(false);
            ac.setFolderSelectable(false);
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
                                Toast.makeText(getContext(), "Add", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.share:
                                Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete:
                                adapterCloudFolder.setSelectable(true);
                                b.searchBox.setVisibility(View.GONE);
                                b.deleteCommit.setVisibility(View.VISIBLE);
                                b.deleteCount.setText("0");
                                ac.setFolderSelectable(true);
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

    private void removeFile() {
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

    }

    private void setRecycler() {
        if (getActivity() != null && getContext() != null)
            adapterCloudFolder = new AdapterCloud(getContext(), getActivity(), CLOUD_TYPE_FOLDER);
        b.recCloudFolder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recCloudFolder.setAdapter(adapterCloudFolder);
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
        b.searchBox.setVisibility(View.VISIBLE);
        b.deleteCommit.setVisibility(View.GONE);
        b.deleteCount.setText("0");
        selectedArray.clear();
        ac.setFolderSelectable(false);
    }
}