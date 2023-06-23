package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.getApi;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.statics.StaticConstants.IN_PROCESS;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterTasks;
import tm.payhas.crm.api.request.RequestUserTasks;
import tm.payhas.crm.api.response.ResponseTasks;
import tm.payhas.crm.databinding.FragmentTasksBinding;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentTasks extends Fragment {
    private FragmentTasksBinding b;
    private AdapterTasks adapterTasks;
    private AccountPreferences accountPreferences;

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
        setRecycler();
        getTasks();
        initListeners();
        return b.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    private void getTasks() {
        ArrayList<String> statuses = new ArrayList<>();
        statuses.add(IN_PROCESS);
        RequestUserTasks requestUserTasks = new RequestUserTasks();
        requestUserTasks.setLimit(10);
        requestUserTasks.setPage(1);
        requestUserTasks.setStatus(statuses);
        Call<ResponseTasks> tasks = getApi().getUserTasks(accountPreferences.getToken(), requestUserTasks);
        tasks.enqueue(new Callback<ResponseTasks>() {
            @Override
            public void onResponse(Call<ResponseTasks> call, Response<ResponseTasks> response) {
                if (response.code() == 200) {
                    adapterTasks.setTasks(response.body().getData().getTasks());
                    Log.e("Size", "onResponse: " + response.body().getData().getTasks().size());
                }

            }

            @Override
            public void onFailure(Call<ResponseTasks> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initListeners() {
        b.favAdd.setOnClickListener(view -> {
            b.favAdd.setEnabled(false);
            addFragment(mainFragmentManager, R.id.secondary_content, FragmentAddTask.newInstance());
            new Handler().postDelayed(() -> b.favAdd.setEnabled(true), 200);
        });
    }

    private void setRecycler() {
        adapterTasks = new AdapterTasks(getContext());
        b.rcvTasks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rcvTasks.setAdapter(adapterTasks);
    }
}