package tm.payhas.crm.fragment;

import static tm.payhas.crm.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterGroupMember;
import tm.payhas.crm.api.response.ResponseGroupInfo;
import tm.payhas.crm.dataModels.DataGroupInfo;
import tm.payhas.crm.databinding.FragmentGroupInfoBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.OnInternetStatus;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentGroupInfo extends Fragment {
    private FragmentGroupInfoBinding b;
    private int groupId;
    private AccountPreferences ac;
    private AdapterGroupMember adapterGroupMember;
    private String groupName;
    private int memberCount;
    private String groupAvatar;

    public static FragmentGroupInfo newInstance(int groupId, int memberCount, String groupName, String groupAvatar) {
        FragmentGroupInfo fragment = new FragmentGroupInfo();
        Bundle args = new Bundle();
        args.putInt("groupId", groupId);
        args.putString("groupName", groupName);
        args.putString("groupAvatar", groupAvatar);
        args.putInt("memberCount", memberCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getInt("groupId");
            groupName = getArguments().getString("groupName");
            memberCount = getArguments().getInt("memberCount");
            groupAvatar = getArguments().getString("groupAvatar");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentGroupInfoBinding.inflate(inflater);
        setUpHelpers();
        setRecycler();
        getGroupMemberInfo();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(view -> getActivity().onBackPressed());
        b.swiper.setOnRefreshListener(() -> {
            b.swiper.setRefreshing(true);
            getGroupMemberInfo();
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

    private void setGroupInfo(DataGroupInfo data) {
        Picasso.get().load(BASE_PHOTO + groupAvatar).placeholder(R.color.primary).into(b.groupAvatar);
        b.groupName.setText(groupName);
        b.participants.setText(String.valueOf(data.getParticipants().size()));
    }

    private void setUpHelpers() {
        ac = new AccountPreferences(getContext());
    }

    private void getGroupMemberInfo() {
        Call<ResponseGroupInfo> call = Common.getApi().getGroupInfo(ac.getToken(), groupId);
        call.enqueue(new Callback<ResponseGroupInfo>() {
            @Override
            public void onResponse(Call<ResponseGroupInfo> call, Response<ResponseGroupInfo> response) {
                if (response.isSuccessful()) {
                    adapterGroupMember.setMemberList(response.body().getData().getParticipants());
                    adapterGroupMember.notifyDataSetChanged();
                    setGroupInfo(response.body().getData());
                    setConnected();
                }
            }

            @Override
            public void onFailure(Call<ResponseGroupInfo> call, Throwable t) {
                setNoInternet();
            }
        });

    }

    private void setConnected() {
        b.swiper.setRefreshing(false);
        OnInternetStatus internetStatusListener = new OnInternetStatus() {
        };
        internetStatusListener.setConnected(b.linearProgressBar, b.noInternet.getRoot(), b.main);
    }

    private void setNoInternet() {
        b.swiper.setRefreshing(false);
        OnInternetStatus internetStatusListener = new OnInternetStatus() {
        };
        internetStatusListener.setNoInternet(b.linearProgressBar, b.noInternet.getRoot(), b.main);
    }

    private void setRecycler() {
        adapterGroupMember = new AdapterGroupMember(getContext());
        b.rvGroupParticipants.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvGroupParticipants.setAdapter(adapterGroupMember);
    }

}