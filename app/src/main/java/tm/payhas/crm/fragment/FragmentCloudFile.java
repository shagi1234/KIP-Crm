package tm.payhas.crm.fragment;

import static tm.payhas.crm.adapters.AdapterCloud.CLOUD_TYPE_FILE;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterCloud;
import tm.payhas.crm.databinding.FragmentCloudFileBinding;

public class FragmentCloudFile extends Fragment {
    private FragmentCloudFileBinding b;
    private AdapterCloud adapterCloudFolder;

    public static FragmentCloudFile newInstance() {
        FragmentCloudFile fragment = new FragmentCloudFile();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentCloudFileBinding.inflate(inflater);
        setRecycler();
        setBackground();
        initListeners();
        return b.getRoot();
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

    @SuppressLint("RestrictedApi")
    private void initListeners() {
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

    private void setRecycler() {
        if (getActivity() != null && getContext() != null)
            adapterCloudFolder = new AdapterCloud(getContext(), getActivity(), CLOUD_TYPE_FILE);
        b.recCloudFile.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recCloudFile.setAdapter(adapterCloudFolder);
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.searchBox, R.color.color_transparent, R.color.primary, 6, false, 1);
    }
}