package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import retrofit2.Call;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterBirthday;
import tm.payhas.crm.adapters.AdapterNews;
import tm.payhas.crm.api.RetrofitCallback;
import tm.payhas.crm.api.response.ResponseDashboard;
import tm.payhas.crm.databinding.FragmentHomeBinding;
import tm.payhas.crm.helpers.Common;


public class FragmentHome extends Fragment {

    private FragmentHomeBinding b;
    private AdapterNews adapterNews;
    private AdapterBirthday adapterBirthday;
    private AdapterBirthday adapterHolidays;
    private int page;
    private final int limit = 10;


    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentHomeBinding.inflate(inflater);
        setBackground();
        initListeners();
        setRecyclerViews();
        getDashboardInfo();
        return b.getRoot();
    }

    private void initListeners() {
        b.swiper.setOnRefreshListener(() -> {
            b.linearProgressBar.setVisibility(View.VISIBLE);
            getDashboardInfo();
        });
        b.addNews.setOnClickListener(view -> {
            b.addNews.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentAddNews.newInstance());
            new Handler().postDelayed(() -> b.addNews.setEnabled(true), 200);
        });
        b.searchCancel.setOnClickListener(view -> b.searchInput.setText(""));
        b.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (b.searchInput.getText().toString().length() > 0) {
                    b.searchCancel.setVisibility(View.VISIBLE);
                } else {
                    b.searchCancel.setVisibility(View.GONE);
                }
                getDashboardInfo();
                b.linearProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setBackground() {
        setBackgroundDrawable(getContext(), b.searchBox, R.color.color_transparent, R.color.primary, 6, false, 1);
    }

    private void setRecyclerViews() {
        setRecyclerNews();
        setRecyclerBirthday();
        setRecyclerHolidays();
    }

    private void setRecyclerHolidays() {
        adapterHolidays = new AdapterBirthday(getContext());
        b.rvHolidays.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.rvHolidays.setAdapter(adapterHolidays);
    }

    private void getDashboardInfo() {
        String search = b.searchInput.getText().toString();
        Call<ResponseDashboard> call = Common.getApi().getDashboard(search, 1, limit);
        call.enqueue(new RetrofitCallback<ResponseDashboard>() {
            @Override
            public void onResponse(ResponseDashboard response) {
                if (response != null && response.isSuccess()) {
                    b.swiper.setRefreshing(false);
                    b.content.setVisibility(View.VISIBLE);
                    b.linearProgressBar.setVisibility(View.GONE);
                    b.progressBar.setVisibility(View.GONE);
                    Log.e("News", "onResponse: " + response.getData().getNews().size());
                    adapterNews.setNews(response.getData().getNews());
                    adapterBirthday.setBirthdays(response.getData().getBirthdays());
                    adapterHolidays.setHolidays(response.getData().getHolidays());
                    Log.e("Dashboard", "onResponse: " + response.getData().getBirthdays().size());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                b.progressBar.setVisibility(View.GONE);
                b.content.setVisibility(View.VISIBLE);
                b.linearProgressBar.setVisibility(View.GONE);
            }
        });
    }


    private void setRecyclerBirthday() {
        adapterBirthday = new AdapterBirthday(getContext());
        b.rvBirthdays.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.rvBirthdays.setAdapter(adapterBirthday);
    }


    private void setRecyclerNews() {
        adapterNews = new AdapterNews(getContext());
        b.rvNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvNews.setAdapter(adapterNews);
    }
}