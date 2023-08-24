package tm.payhas.crm.presentation.view.fragment;

import static android.view.Gravity.CENTER;
import static tm.payhas.crm.presentation.view.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.Common.getApi;
import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentProjectsBinding;
import tm.payhas.crm.presentation.view.adapters.AdapterFilter;
import tm.payhas.crm.presentation.view.adapters.AdapterProjects;
import tm.payhas.crm.data.remote.api.request.RequestMyProjects;
import tm.payhas.crm.data.remote.api.response.ResponseFilter;
import tm.payhas.crm.data.remote.api.response.ResponseProjects;
import tm.payhas.crm.domain.model.DataFilter;
import tm.payhas.crm.domain.interfaces.OnInternetStatus;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class FragmentProjects extends Fragment {
    private FragmentProjectsBinding b;
    private AdapterProjects adapterProjects;
    private AdapterFilter adapterFilter;
    private AccountPreferences ac;
    private static final String TAG = "FRAGMENT_PROJECTS";
    private ArrayList<DataFilter> varianceItems;
    private ArrayList<DataFilter> selectedVarianceItems = new ArrayList<>();
    private Dialog dialog;
    private ArrayList<String> filterToSend;
    private int page = 1;
    private int limit = 10;

    public static FragmentProjects newInstance() {
        FragmentProjects fragment = new FragmentProjects();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentProjectsBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        setHelpers();
        setDialog();
        setRecycler();
        initListeners();
        getProjects();
        getFilterArray();
        return b.getRoot();
    }

    private void setHelpers() {
        ac = AccountPreferences.newInstance(getContext());
    }

    private void getFilterArray() {
        Call<ResponseFilter> call = getApi().getFilter();
        call.enqueue(new Callback<ResponseFilter>() {
            @Override
            public void onResponse(Call<ResponseFilter> call, Response<ResponseFilter> response) {
                if (response.isSuccessful()) {
                    if (varianceItems == null) {
                        varianceItems = new ArrayList<>();
                    }
                    varianceItems.addAll(response.body().getData());
                    setAdapterFilterList(varianceItems);
                }
            }

            @Override
            public void onFailure(Call<ResponseFilter> call, Throwable t) {

            }
        });
    }

    private void setAdapterFilterList(ArrayList<DataFilter> varianceItems) {
        adapterFilter.setVarianceItems(varianceItems);
        adapterFilter.notifyDataSetChanged();
    }

    private void setDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.project_tasks_filter);

        TextView done = dialog.findViewById(R.id.done_button);
        TextView selectDeselectText = dialog.findViewById(R.id.select_deselect_text);
        RecyclerView rvFilter = dialog.findViewById(R.id.rv_filter);
        setFilterAdapter(rvFilter, selectDeselectText);

        // Add a click listener to the "Select All" button or item
        selectDeselectText.setOnClickListener(v -> {
            boolean allSelected = true; // Track the current selection state

            // Check if all items are currently selected or not
            for (DataFilter item : varianceItems) {
                if (!item.isSelected()) {
                    allSelected = false;
                    break;
                }
            }

            // Update the selection state of all items based on the current state
            for (DataFilter item : varianceItems) {
                item.setSelected(!allSelected);
            }

            adapterFilter.notifyDataSetChanged();
            // Change the button text based on the selection state
            if (allSelected) {
                selectDeselectText.setText("Select All");
            } else {
                selectDeselectText.setText("Deselect All");
            }// Update the RecyclerView
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.swipe.setRefreshing(true);
                selectedVarianceItems = adapterFilter.getSelectedArray();
                getProjects();
                dialog.dismiss();
            }
        });


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(CENTER);

    }

    private void setFilterAdapter(RecyclerView rvFilter, TextView selectDeselectText) {
        adapterFilter = new AdapterFilter(getContext(), selectDeselectText);
        rvFilter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvFilter.setAdapter(adapterFilter);
        // Retrieve the string array from resources
    }


    private void initListeners() {
        b.btnFilter.setOnClickListener(view -> dialog.show());
        b.swipe.setOnRefreshListener(() -> getProjects());
        b.favAdd.setOnClickListener(view -> {
            b.favAdd.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentAddProject.newInstance(false, 0));
            new Handler().postDelayed(() -> b.favAdd.setEnabled(true), 200);
        });
    }

    private String[] createStringValueArray(ArrayList<String> stringList) {
        String[] stringArray = new String[stringList.size()];
        stringList.toArray(stringArray);
        return stringArray;
    }

    private ArrayList<String> createValueList(ArrayList<DataFilter> customObjectsList) {
        ArrayList<String> valueList = new ArrayList<>();
        for (DataFilter customObject : customObjectsList) {
            // Get the 'value' property from each CustomObject and add it to the valueList
            String value = customObject.getValue();
            valueList.add(value);
        }
        return valueList;
    }

    private void getProjects() {
        RequestMyProjects requestMyProjects = new RequestMyProjects();
        if (selectedVarianceItems != null) {
            if (selectedVarianceItems.size() != 0)
                requestMyProjects.setStatusFilter(createValueList(selectedVarianceItems));
        }
        requestMyProjects.setLimit(limit);
        requestMyProjects.setPage(page);
        Call<ResponseProjects> call = getApi().getMyProjects(ac.getToken(), requestMyProjects);

        call.enqueue(new Callback<ResponseProjects>() {
            @Override
            public void onResponse(Call<ResponseProjects> call, Response<ResponseProjects> response) {
                if (response.isSuccessful() || response.body() != null) {
                    adapterProjects.setProjects(response.body().getData().getRows());
                    b.swipe.setRefreshing(false);
                    OnInternetStatus internetStatusListener = new OnInternetStatus() {};
                    internetStatusListener.setConnected(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
                }

            }

            @Override
            public void onFailure(Call<ResponseProjects> call, Throwable t) {
                b.swipe.setRefreshing(false);
                OnInternetStatus internetStatusListener = new OnInternetStatus() {};
                internetStatusListener.setNoInternet(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
            }
        });


    }

    private void setRecycler() {
        adapterProjects = new AdapterProjects(getContext(), getActivity());
        b.rcvProjects.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rcvProjects.setAdapter(adapterProjects);
    }
}