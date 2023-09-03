package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.data.remote.api.network.Network.BASE_PHOTO;
import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.Common.getApi;
import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.domain.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentCreateGroupBinding;
import tm.payhas.crm.domain.useCases.UseCaseGroup;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.adapters.AdapterCreateGroup;
import tm.payhas.crm.data.remote.api.request.RequestNewGroup;
import tm.payhas.crm.data.remote.api.response.ResponseOneGroup;
import tm.payhas.crm.data.remote.api.response.ResponseUsersList;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.domain.helpers.SelectedMedia;
import tm.payhas.crm.domain.interfaces.OnUserCountChangeListener;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class FragmentCreateGroup extends Fragment implements OnUserCountChangeListener {
    private FragmentCreateGroupBinding b;
    private AdapterCreateGroup adapterCreateGroup;
    private String avatarUrl;
    private ArrayList<Integer> selectedUserList = new ArrayList<>();
    private UseCaseGroup useCaseGroup;

    public static FragmentCreateGroup newInstance() {
        FragmentCreateGroup fragment = new FragmentCreateGroup();
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
    public void onResume() {
        super.onResume();
        setPadding(b.main, 0, 50, 0, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCreateGroupBinding.inflate(inflater);
        setRecycler();
        getMembers();
        initListeners();
        useCaseGroup = new UseCaseGroup(getContext());
        return b.getRoot();
    }

    private void initListeners() {
        b.doneButton.setOnClickListener(view -> {
            b.doneButton.setEnabled(false);
            if (adapterCreateGroup != null)
                selectedUserList = adapterCreateGroup.getSelectedUserList();
            createGroup(b.groupName.getText().toString());
            hideSoftKeyboard(getActivity());
            new Handler().postDelayed(() -> b.doneButton.setEnabled(true), 200);
        });
        b.groupAvatarClicker.setOnClickListener(view -> {
            b.groupAvatarClicker.setEnabled(false);
            SelectedMedia.getArrayList().clear();
            addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentOpenGallery.newInstance(1, 0, FragmentOpenGallery.SINGLE, 0));
            new Handler().postDelayed(() -> b.groupAvatarClicker.setEnabled(true), 200);
        });
        b.cancelButton.setOnClickListener(view -> {
            b.cancelButton.setEnabled(false);
            getActivity().onBackPressed();
        });
    }

    private void setRecycler() {
        adapterCreateGroup = new AdapterCreateGroup(getContext());
        adapterCreateGroup.setUserCountChangeListener(this);
        b.rvMembers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvMembers.setAdapter(adapterCreateGroup);
    }

    private void getMembers() {
        Call<ResponseUsersList> call = Common.getApi().getAllUsers();
        call.enqueue(new Callback<ResponseUsersList>() {
            @Override
            public void onResponse(Call<ResponseUsersList> call, Response<ResponseUsersList> response) {
                if (response.isSuccessful()) {
                    adapterCreateGroup.setMembers(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseUsersList> call, Throwable t) {
                Log.e("Create Group", "onFailure: " + t.getMessage());
            }
        });
    }


    private void createGroup(String s) {
        if (!(s == null || s.equals("") || selectedUserList.size() == 0)) {
            RequestNewGroup requestNewGroup = new RequestNewGroup(s, selectedUserList, avatarUrl, "group");
            Call<ResponseOneGroup> call = getApi().createGroup(AccountPreferences.newInstance(getContext()).getToken(), requestNewGroup);
            call.enqueue(new Callback<ResponseOneGroup>() {
                @Override
                public void onResponse(Call<ResponseOneGroup> call, Response<ResponseOneGroup> response) {
                    if (response.isSuccessful() || response.body() != null) {
                        FragmentGroups groupInfoFragment = (FragmentGroups) getParentFragmentManager().findFragmentByTag(FragmentGroups.class.getSimpleName());
                        if (groupInfoFragment != null) {
                            groupInfoFragment.refresh(); // Call the method to refresh data in the GroupInfoFragment
                        }
                        useCaseGroup.insertGroup(response.body().getData());
                        new Handler().postDelayed(() -> getActivity().onBackPressed(), 500);

                    }
                }

                @Override
                public void onFailure(Call<ResponseOneGroup> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(getContext(), "Access Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserCountChange(String count) {
        b.memberCount.setText(count);
        Log.e("Count", "onUserCountChange: " + count);
    }

    @Override
    public void avatarUrl(String avatar) {
        avatarUrl = avatar;
        Picasso.get().load(BASE_PHOTO + avatar).placeholder(R.color.primary).into(b.groupAvatar);
    }
}