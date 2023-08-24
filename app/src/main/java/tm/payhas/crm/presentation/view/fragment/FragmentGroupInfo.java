package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.presentation.view.activity.ActivityMain.mainFragmentManager;

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
import tm.payhas.crm.data.localdb.entity.EntityGroup;
import tm.payhas.crm.databinding.FragmentGroupInfoBinding;
import tm.payhas.crm.domain.useCases.UseCaseGroup;
import tm.payhas.crm.presentation.view.adapters.AdapterGroupMember;
import tm.payhas.crm.data.remote.api.response.ResponseGroupInfo;
import tm.payhas.crm.domain.model.DataGroupInfo;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.domain.interfaces.OnInternetStatus;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class FragmentGroupInfo extends Fragment {
    private FragmentGroupInfoBinding b;
    private int groupId;
    private AccountPreferences ac;
    private AdapterGroupMember adapterGroupMember;
    private String groupName;
    private String groupAvatar;
    private UseCaseGroup useCaseGroup;

    public static FragmentGroupInfo newInstance(int groupId) {
        FragmentGroupInfo fragment = new FragmentGroupInfo();
        Bundle args = new Bundle();
        args.putInt("groupId", groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getInt("groupId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        b.groupAvatar.setOnClickListener(view -> {
            b.groupAvatar.setEnabled(false);
            addFragment(mainFragmentManager, R.id.main_content, FragmentPhotoItem.newInstance(groupAvatar));
            new Handler().postDelayed(() -> b.groupAvatar.setEnabled(true), 200);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.swiper, 0, 50, 0, 0), 100);
    }

    private void setGroupInfo(DataGroupInfo data) {
        EntityGroup entityGroup = useCaseGroup.getOneGroupInfo(groupId);
        groupName = entityGroup.getName();
        groupAvatar = entityGroup.getAvatar();
        Picasso.get().load(BASE_PHOTO + entityGroup.getAvatar()).placeholder(R.color.primary).into(b.groupAvatar);
        b.groupName.setText(groupName);
        b.participants.setText(String.valueOf(data.getParticipants().size()));
    }

    private void setUpHelpers() {
        useCaseGroup = new UseCaseGroup(getContext());
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