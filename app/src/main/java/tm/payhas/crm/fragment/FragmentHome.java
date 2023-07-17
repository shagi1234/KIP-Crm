package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;

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
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterBirthday;
import tm.payhas.crm.adapters.AdapterNews;
import tm.payhas.crm.api.RetrofitCallback;
import tm.payhas.crm.api.request.RequestFcmToken;
import tm.payhas.crm.api.response.ResponseDashboard;
import tm.payhas.crm.api.response.ResponseFcmToken;
import tm.payhas.crm.databinding.FragmentHomeBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.preference.AccountPreferences;
import tm.payhas.crm.preference.FcmPreferences;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding b;
    private AdapterNews adapterNews;
    private AdapterBirthday adapterBirthday;
    private AdapterBirthday adapterHolidays;
    private AccountPreferences ac;
    private FcmPreferences fcmPreferences;
    private final int limit = 10;

    public static FragmentHome newInstance() {
        return new FragmentHome();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentHomeBinding.inflate(inflater, container, false);
        setHelpers();
        setBackground();
        setFcmToken();
        initListeners();
        setRecyclerViews();
        getDashboardInfo();
        return b.getRoot();
    }

    private void setHelpers() {
        ac = new AccountPreferences(getContext());
        fcmPreferences = new FcmPreferences(getContext());
    }

    private void setFcmToken() {
        if (!fcmPreferences.getIsSent()) {
            RequestFcmToken requestFcmToken = new RequestFcmToken();
            requestFcmToken.setFcmtoken(fcmPreferences.getFcm());
            Common.getApi().setFcmToken(ac.getToken(), requestFcmToken).enqueue(new Callback<ResponseFcmToken>() {
                @Override
                public void onResponse(Call<ResponseFcmToken> call, Response<ResponseFcmToken> response) {
                    if (response.isSuccessful()) {
                        Log.e("FCM_TOKEN", "onResponse: " + "Succes");
                        fcmPreferences.setIsSent(true);
                    }
                }

                @Override
                public void onFailure(Call<ResponseFcmToken> call, Throwable t) {
                    Log.e("FCM_TOKEN", "onFailure: " + t.getMessage());
                }
            });
        }
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

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
            public void afterTextChanged(Editable editable) {}
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
        Common.getApi().getDashboard(search, 1, limit).enqueue(new RetrofitCallback<ResponseDashboard>() {
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
