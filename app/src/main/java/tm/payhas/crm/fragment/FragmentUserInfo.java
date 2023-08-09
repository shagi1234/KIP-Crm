package tm.payhas.crm.fragment;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import tm.payhas.crm.R;
import tm.payhas.crm.api.RetrofitCallback;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.api.response.ResponseUserInfo;
import tm.payhas.crm.databinding.FragmentUserInfoBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.OnInternetStatus;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentUserInfo extends Fragment {
    private FragmentUserInfoBinding b;
    private DtoUserInfo currentUserInfo;
    private AccountPreferences accountPreferences;
    private int userId;
    private String avatar;

    public static FragmentUserInfo newInstance(int userId) {
        FragmentUserInfo fragment = new FragmentUserInfo();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentUserInfoBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        setConstants();
        getUserInfo();
        initListeners();
        return b.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hideSoftKeyboard(getActivity());
    }

    private void setConstants() {
        accountPreferences = new AccountPreferences(getContext());
        hideSoftKeyboard(getActivity());
    }

    private void getUserInfo() {
        Call<ResponseUserInfo> call = Common.getApi().getUserInfo(accountPreferences.getToken(), userId);
        call.enqueue(new RetrofitCallback<ResponseUserInfo>() {
            @Override
            public void onResponse(ResponseUserInfo response) {
                if (response.isSuccess()) {
                    setConnected();
                    b.info.setVisibility(View.VISIBLE);
                    currentUserInfo = response.getData();
                    setUserInfo();
                    avatar = response.getData().getAvatar();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                setNoInternet();
            }
        });
    }

    private void setConnected() {
        b.swiper.setRefreshing(false);
        OnInternetStatus internetStatusListener = new OnInternetStatus() {
        };
        internetStatusListener.setConnected(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
    }

    private void setNoInternet() {
        b.swiper.setRefreshing(false);
        OnInternetStatus internetStatusListener = new OnInternetStatus() {
        };
        internetStatusListener.setNoInternet(b.progressBar.getRoot(), b.noInternet.getRoot(), b.main);
    }

    private void initListeners() {
        b.swiper.setOnRefreshListener(() -> {
            b.swiper.setRefreshing(true);
            getUserInfo();
        });
        b.backBtn.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
            b.backBtn.setEnabled(false);
        });
        b.profileImage.setOnClickListener(view -> {
            b.profileImage.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentPhotoItem.newInstance(avatar));
            new Handler().postDelayed(() -> b.profileImage.setEnabled(true), 200);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.swiper,
                0,
                50,
                0,
                0), 100);
    }

    private void setUserInfo() {
        Picasso.get().load(BASE_PHOTO + currentUserInfo.getAvatar()).placeholder(R.color.primary).into(b.profileImage);
        b.profileName.setText(currentUserInfo.getPersonalData().getName());
        b.profileSurname.setText(currentUserInfo.getPersonalData().getSurname());
        b.profileEmail.setText(currentUserInfo.getEmail());
        b.profileBirthday.setText(currentUserInfo.getPersonalData().getBirthday());
        b.profilePhoneNumber.setText(currentUserInfo.getMobilePhoneNumber());
    }
}