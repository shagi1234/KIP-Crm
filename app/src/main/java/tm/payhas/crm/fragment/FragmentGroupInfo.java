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
import tm.payhas.crm.databinding.FragmentGroupInfoBinding;
import tm.payhas.crm.helpers.Common;
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
        setGroupInfo();
        setUpHelpers();
        setRecycler();
        getGroupMemberInfo();
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

    private void setGroupInfo() {
        Picasso.get().load(BASE_PHOTO + groupAvatar).placeholder(R.color.primary).into(b.groupAvatar);
        b.groupName.setText(groupName);
        b.participants.setText(String.valueOf(memberCount));
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
                    b.linearProgressBar.setVisibility(View.GONE);
                    adapterGroupMember.setMemberList(response.body().getData().getParticipants());
                    adapterGroupMember.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseGroupInfo> call, Throwable t) {

            }
        });

    }

    private void setRecycler() {
        adapterGroupMember = new AdapterGroupMember(getContext());
        b.rvGroupParticipants.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvGroupParticipants.setAdapter(adapterGroupMember);
    }

}