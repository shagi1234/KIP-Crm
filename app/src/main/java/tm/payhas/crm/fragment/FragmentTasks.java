package tm.payhas.crm.fragment;

import static android.view.Gravity.CENTER;
import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.getApi;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.statics.StaticConstants.NOT_STARTED;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import tm.payhas.crm.adapters.AdapterFilter;
import tm.payhas.crm.adapters.AdapterTasks;
import tm.payhas.crm.api.request.RequestUserTasks;
import tm.payhas.crm.api.response.ResponseFilter;
import tm.payhas.crm.api.response.ResponseTasks;
import tm.payhas.crm.dataModels.DataFilter;
import tm.payhas.crm.databinding.FragmentTasksBinding;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentTasks extends Fragment {
    private FragmentTasksBinding b;
    private AdapterTasks adapterTasks;
    private AccountPreferences accountPreferences;
    private Dialog dialog;
    private AdapterFilter adapterFilter;
    private ArrayList<DataFilter> varianceItems;
    private ArrayList<DataFilter> selectedVarianceItems = new ArrayList<>();


    public static FragmentTasks newInstance() {
        FragmentTasks fragment = new FragmentTasks();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentTasksBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        accountPreferences = new AccountPreferences(getContext());
        adapterTasks = new AdapterTasks(getContext());
        setDialog();
        setRecycler();
        getFilterArray();
        getTasks();
        initListeners();
        return b.getRoot();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    private void setAdapterFilterList(ArrayList<DataFilter> varianceItems) {
        adapterFilter.setVarianceItems(varianceItems);
        adapterFilter.notifyDataSetChanged();
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


        done.setOnClickListener(view -> {
            b.swipe.setRefreshing(true);
            selectedVarianceItems = adapterFilter.getSelectedArray();
            getTasks();
            dialog.dismiss();
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

    private ArrayList<String> createValueList(ArrayList<DataFilter> customObjectsList) {
        ArrayList<String> valueList = new ArrayList<>();
        for (DataFilter customObject : customObjectsList) {
            // Get the 'value' property from each CustomObject and add it to the valueList
            String value = customObject.getValue();
            valueList.add(value);
        }
        return valueList;
    }

    private void getTasks() {
        ArrayList<String> statuses = new ArrayList<>();
        statuses.add(NOT_STARTED);
        RequestUserTasks requestUserTasks = new RequestUserTasks();
        requestUserTasks.setLimit(50);
        requestUserTasks.setPage(1);
        if (selectedVarianceItems != null) {
            if (selectedVarianceItems.size() != 0)
                requestUserTasks.setStatus(createValueList(selectedVarianceItems));
        }
        Call<ResponseTasks> tasks = getApi().getUserTasks(accountPreferences.getToken(), requestUserTasks);
        tasks.enqueue(new Callback<ResponseTasks>() {
            @Override
            public void onResponse(Call<ResponseTasks> call, Response<ResponseTasks> response) {
                if (response.code() == 200) {
                    b.main.setVisibility(View.VISIBLE);
                    b.swipe.setRefreshing(false);
                    b.progressBar.setVisibility(View.GONE);
                    adapterTasks.setTasks(response.body().getData().getTasks());
                    Log.e("Size", "onResponse: " + response.body().getData().getTasks().size());
                }

            }

            @Override
            public void onFailure(Call<ResponseTasks> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                b.main.setVisibility(View.VISIBLE);
                b.progressBar.setVisibility(View.GONE);
                b.swipe.setRefreshing(false);
            }
        });
    }

    private void initListeners() {
        b.btnFilter.setOnClickListener(view -> dialog.show());
        b.swipe.setOnRefreshListener(() -> getTasks());
        b.favAdd.setOnClickListener(view -> {
            b.favAdd.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentAddTask.newInstance(0,0));
            new Handler().postDelayed(() -> b.favAdd.setEnabled(true), 200);
        });
    }

    private void setRecycler() {
        adapterTasks = new AdapterTasks(getContext());
        b.rcvTasks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rcvTasks.setAdapter(adapterTasks);
    }
}