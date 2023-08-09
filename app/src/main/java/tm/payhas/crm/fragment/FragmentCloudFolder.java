package tm.payhas.crm.fragment;

import static tm.payhas.crm.adapters.AdapterCloud.CLOUD_TYPE_FOLDER;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterCloud;
import tm.payhas.crm.api.request.RequestNewFolder;
import tm.payhas.crm.api.response.ResponseDataFolder;
import tm.payhas.crm.api.response.ResponseDeleteFile;
import tm.payhas.crm.api.response.ResponseNewFolder;
import tm.payhas.crm.dataModels.DataFolder;
import tm.payhas.crm.databinding.FragmentCloudFolderBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.helpers.StaticMethods;
import tm.payhas.crm.interfaces.DataFileSelectedListener;
import tm.payhas.crm.interfaces.OnInternetStatus;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentCloudFolder extends Fragment implements DataFileSelectedListener {
    private FragmentCloudFolderBinding b;
    private AdapterCloud adapterCloudFolder;
    private ArrayList<DataFolder> selectedArray = new ArrayList<>();
    private AccountPreferences ac;
    private String colorToSend = null;
    private Dialog dialog;

    public static FragmentCloudFolder newInstance() {
        return new FragmentCloudFolder();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentCloudFolderBinding.inflate(inflater, container, false);
        setHelpers();
        StaticMethods.hideSoftKeyboard(getActivity());
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
                    if (response.body() != null && response.body().getData() != null) {
                        adapterCloudFolder.setAll(response.body().getData());
                    }
                    OnInternetStatus internetStatusListener = new OnInternetStatus() {
                    };
                    internetStatusListener.setConnected(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseDataFolder> call, @NonNull Throwable t) {
                b.linearProgressBar.setVisibility(View.GONE);
                b.swiper.setRefreshing(false);
                OnInternetStatus internetStatusListener = new OnInternetStatus() {
                };
                internetStatusListener.setNoInternet(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        StaticMethods.hideSoftKeyboard(getActivity());
    }

    @SuppressLint("RestrictedApi")
    private void initListeners() {
        b.favAdd.setOnClickListener(view -> showDialog());

        b.swiper.setOnRefreshListener(this::getCloudFolders);

        b.deleteCommit.setOnClickListener(view -> showDeleteDialog());

        if (getContext() != null) {
            b.optionMenu.setOnClickListener(view -> {
                b.optionMenu.setEnabled(false);
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(getContext());
                new MenuInflater(getContext()).inflate(R.menu.cloud_menu, menuBuilder);

                MenuPopupHelper popupHelper = new MenuPopupHelper(getContext(), menuBuilder, b.optionMenu);
                popupHelper.setForceShowIcon(true);
                popupHelper.show();

                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add:
                                showDialog();
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

    private void showDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_folder);

        EditText folderName = dialog.findViewById(R.id.folder_name_input);
        TextView done = dialog.findViewById(R.id.done_button);
        TextView cancel = dialog.findViewById(R.id.cancel_button);
        RoundedImageView var1 = dialog.findViewById(R.id.var_color1);
        RoundedImageView var2 = dialog.findViewById(R.id.var_color2);
        RoundedImageView var3 = dialog.findViewById(R.id.var_color3);
        RoundedImageView var4 = dialog.findViewById(R.id.var_color4);
        RoundedImageView folderColor = dialog.findViewById(R.id.folder_color);
        View[] borderViews = {
                dialog.findViewById(R.id.border_var1),
                dialog.findViewById(R.id.border_var2),
                dialog.findViewById(R.id.border_var3),
                dialog.findViewById(R.id.border_var4)
        };

        RoundedImageView[] colorViews = {var1, var2, var3, var4};

        int[] colors = {
                R.color.color_var1,
                R.color.color_var2,
                R.color.color_var3,
                R.color.color_var4
        };

        for (int i = 0; i < colorViews.length; i++) {
            int colorPrimary = getResources().getColor(colors[i]);
            String hexColor = "#" + Integer.toHexString(colorPrimary);
            colorViews[i].setColorFilter(colorPrimary);
            int finalI = i;
            colorViews[i].setOnClickListener(view -> {
                colorToSend = hexColor;
                for (View borderView : borderViews) {
                    borderView.setVisibility(View.GONE);
                }
                borderViews[finalI].setVisibility(View.VISIBLE);
                folderColor.setColorFilter(colorPrimary);
            });
        }

        cancel.setOnClickListener(view -> dialog.dismiss());

        done.setOnClickListener(view -> createNewFolder(folderName.getText().toString(), colorToSend));

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void showDeleteDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_dialog);

        TextView done = dialog.findViewById(R.id.done_button);
        TextView cancel = dialog.findViewById(R.id.cancel_button);

        cancel.setOnClickListener(view -> dialog.dismiss());

        done.setOnClickListener(view -> removeFile());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void createNewFolder(String folderName, String colorToSend) {
        RequestNewFolder requestNewFolder = new RequestNewFolder();
        requestNewFolder.setFolderName(folderName);
        requestNewFolder.setColor(colorToSend);

        Call<ResponseNewFolder> call = Common.getApi().createNewFolder(requestNewFolder);
        call.enqueue(new Callback<ResponseNewFolder>() {
            @Override
            public void onResponse(Call<ResponseNewFolder> call, Response<ResponseNewFolder> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseNewFolder> call, Throwable t) {

            }
        });
    }

    private void removeFile() {
        b.linearProgressBar.setVisibility(View.VISIBLE);
        b.recCloudFolder.setAlpha(0.5f);

        for (DataFolder selected : selectedArray) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("fileUrl", selected.getFileUrl());
            Call<ResponseDeleteFile> call = Common.getApi().removeFile(jsonObject);
            call.enqueue(new Callback<ResponseDeleteFile>() {
                @Override
                public void onResponse(Call<ResponseDeleteFile> call, Response<ResponseDeleteFile> response) {
                    if (response.isSuccessful()) {
                        getCloudFolders();
                    }
                }

                @Override
                public void onFailure(Call<ResponseDeleteFile> call, Throwable t) {

                }
            });
        }

        selectedArray.clear();
        b.deleteCommit.setVisibility(View.GONE);
        b.searchBox.setVisibility(View.VISIBLE);
        adapterCloudFolder.setSelectable(false);
        ac.setFolderSelectable(false);
        dialog.dismiss();
    }

    private void setRecycler() {
        adapterCloudFolder = new AdapterCloud(getContext(), getActivity(), CLOUD_TYPE_FOLDER);
        b.recCloudFolder.setLayoutManager(new LinearLayoutManager(getContext()));
        b.recCloudFolder.setAdapter(adapterCloudFolder);
    }

    private void setBackground() {
        StaticMethods.setBackgroundDrawable(getContext(), b.searchBox, R.color.color_transparent, R.color.primary, 6, false, 1);
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
