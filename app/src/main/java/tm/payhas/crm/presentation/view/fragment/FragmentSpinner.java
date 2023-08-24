package tm.payhas.crm.presentation.view.fragment;

import static tm.payhas.crm.domain.helpers.StaticMethods.hideSoftKeyboard;
import static tm.payhas.crm.domain.helpers.StaticMethods.setPadding;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.databinding.FragmentSpinnerBinding;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.adapters.AdapterLanguage;
import tm.payhas.crm.presentation.view.adapters.AdapterListProjects;
import tm.payhas.crm.presentation.view.adapters.AdapterSpinnerUsers;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.data.remote.api.response.ResponseAllProjects;
import tm.payhas.crm.data.remote.api.response.ResponseTaskMembers;
import tm.payhas.crm.data.remote.api.response.ResponseUsersList;
import tm.payhas.crm.domain.helpers.Common;
import tm.payhas.crm.domain.helpers.StaticMethods;
import tm.payhas.crm.domain.interfaces.AddTask;
import tm.payhas.crm.domain.interfaces.HelperAddProject;
import tm.payhas.crm.domain.interfaces.HelperChecklist;
import tm.payhas.crm.domain.interfaces.OnInternetStatus;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class FragmentSpinner extends Fragment {
    public static final int TASK_MEMBERS = 7;
    private FragmentSpinnerBinding b;
    private int type;
    private int projectId;
    private int taskId;
    private ArrayList<Integer> selectedUserList = new ArrayList<>();
    public static final int PROJECT_CONNECTED = 1;
    public static final int PROJECTS = 2;
    public static final int RESPONSIBLE = 3;
    public static final int OBSERVERS = 4;
    public static final int PROJECT_MEMBERS = 5;
    public static final int PROJECT_EXECUTOR = 6;
    public static final int LANGUAGE = 8;
    private AdapterListProjects adapterProjects;
    private AdapterSpinnerUsers adapterProjectUsers;
    private AdapterSpinnerUsers adapterExecutor;
    private AccountPreferences accountPreferences;
    private ArrayList<String> languages = new ArrayList<>();
    private AdapterLanguage adapterLanguage;


    // TODO: Rename and change types and number of parameters
    public static FragmentSpinner newInstance(int type, int projectId, int taskId, ArrayList<Integer> userList) {
        FragmentSpinner fragment = new FragmentSpinner();
        Bundle args = new Bundle();
        args.putInt("roomId", type);
        args.putInt("projectId", projectId);
        args.putInt("taskId", taskId);
        args.putIntegerArrayList("userList", userList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("roomId");
            projectId = getArguments().getInt("projectId");
            taskId = getArguments().getInt("taskId");
            selectedUserList = getArguments().getIntegerArrayList("userList");
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
        b = FragmentSpinnerBinding.inflate(inflater);
        hideSoftKeyboard(getActivity());
        accountPreferences = new AccountPreferences(getContext());
        setUpSpinner();
        initListeners();
        setBackground();
        return b.getRoot();
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

    private void setBackground() {
        StaticMethods.setBackgroundDrawable(getContext(), b.searchBox, R.color.color_transparent, R.color.primary, 6, false, 1);
    }

    private void setAdapterLanguage() {
        b.rvSpinnerUsers.setVisibility(View.VISIBLE);
        adapterLanguage = new AdapterLanguage(getContext(), getActivity());
        b.rvSpinnerUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvSpinnerUsers.setAdapter(adapterLanguage);
    }

    private void setUpSpinner() {
        switch (type) {
            case PROJECTS:
                b.customTitleBar.setEnabled(false);
                b.searchBox.setVisibility(View.GONE);
                setRecyclerProject();
                getProjects();
                break;
            case PROJECT_CONNECTED:
                b.searchBox.setVisibility(View.GONE);
                break;
            case OBSERVERS:
                setRecyclerProjectUsers();
                getObservers();
                break;
            case TASK_MEMBERS:
                setRecyclerProjectUsers();
                getTaskMembers();
                break;
            case RESPONSIBLE:
                setRecyclerProjectUsers();
                getResponsible();
                break;
            case PROJECT_MEMBERS:
                setRecyclerProjectUsers();
                getProjectMembers();
                break;
            case PROJECT_EXECUTOR:
                b.customTitleBar.setEnabled(false);
                getProjectExecutors();
                getAllUsers();
                break;
            case LANGUAGE:
                b.spinnerText.setText(R.string.language);
                b.customTitleBar.setEnabled(false);
                b.searchBox.setVisibility(View.GONE);
                setAdapterLanguage();
                getLanguages();
                break;

        }

    }

    private void getAllUsers() {
        Call<ResponseUsersList> call = Common.getApi().getAllUsers();
        call.enqueue(new Callback<ResponseUsersList>() {
            @Override
            public void onResponse(Call<ResponseUsersList> call, Response<ResponseUsersList> response) {
                if (response.isSuccessful()) {
                    adapterExecutor.setUsersList(response.body().getData());
                    setConnected();
                }
            }

            @Override
            public void onFailure(Call<ResponseUsersList> call, Throwable t) {
                setNoInternet();
            }
        });
    }

    private void getLanguages() {
        setConnected();
        languages.clear();
        String[] languagesToSet = getResources().getStringArray(R.array.languages);
        languages.addAll(Arrays.asList(languagesToSet));
        adapterLanguage.setLanguages(languages);
    }

    private void getTaskMembers() {
        Call<ResponseTaskMembers> call = Common.getApi().getTaskMembers(accountPreferences.getToken(), taskId);
        call.enqueue(new Callback<ResponseTaskMembers>() {
            @Override
            public void onResponse(Call<ResponseTaskMembers> call, Response<ResponseTaskMembers> response) {
                if (response.isSuccessful()) {
                    adapterProjectUsers.setUsersList(response.body().getData().getResponsible());
                    setConnected();
                }
            }

            @Override
            public void onFailure(Call<ResponseTaskMembers> call, Throwable t) {
                setNoInternet();
            }
        });
    }

    private void getProjectExecutors() {
        b.rvSpinnerUsers.setVisibility(View.VISIBLE);
        adapterExecutor = new AdapterSpinnerUsers(getContext(), getActivity(), AdapterSpinnerUsers.SINGULAR);
        b.rvSpinnerUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvSpinnerUsers.setAdapter(adapterExecutor);
    }

    private void getProjectMembers() {
        adapterProjectUsers.setSelectedUserList(selectedUserList);
        Call<ResponseUsersList> call = Common.getApi().getAllUsers();
        call.enqueue(new Callback<ResponseUsersList>() {
            @Override
            public void onResponse(Call<ResponseUsersList> call, Response<ResponseUsersList> response) {
                if (response.isSuccessful()) {
                    adapterProjectUsers.setUsersList(response.body().getData());
                    setConnected();
                }
            }

            @Override
            public void onFailure(Call<ResponseUsersList> call, Throwable t) {
                setNoInternet();
            }
        });
    }

    private void getResponsible() {
        getObservers();
    }

    private void getObservers() {
        adapterProjectUsers.setSelectedUserList(selectedUserList);
        Call<ResponseUsersList> call = Common.getApi().getProjectsUserList(projectId);
        call.enqueue(new Callback<ResponseUsersList>() {
            @Override
            public void onResponse(Call<ResponseUsersList> call, Response<ResponseUsersList> response) {
                if (response.isSuccessful()) {
                    adapterProjectUsers.setUsersList(response.body().getData());
                    setConnected();
                }
            }

            @Override
            public void onFailure(Call<ResponseUsersList> call, Throwable t) {
                setNoInternet();
            }
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

    private void initListeners() {
        b.swiper.setOnRefreshListener(() -> {
            b.swiper.setEnabled(true);
            setUpSpinner();
        });
        b.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                switch (type) {
                    case PROJECT_MEMBERS:
                    case TASK_MEMBERS:
                    case RESPONSIBLE:
                    case OBSERVERS:
                        adapterProjectUsers.getFilter().filter(charSequence);
                        adapterProjectUsers.setSearchText(b.searchInput.getText().toString());
                        break;
                    case PROJECT_EXECUTOR:
                        adapterExecutor.getFilter().filter(charSequence);
                        adapterExecutor.setSearchText(b.searchInput.getText().toString());
                        break;

                    default:
                        break;
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        b.customTitleBar
                .setOnClickListener(view -> {
                    ArrayList<EntityUserInfo> selectedUsers = new ArrayList<>();
                    if (adapterProjectUsers.getSelectedUsers() != null)
                        selectedUsers = adapterProjectUsers.getSelectedUsers();
                    getActivity().onBackPressed();

                    if (type == RESPONSIBLE) {
                        Fragment addTask = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentAddTask.class.getSimpleName());
                        if (addTask instanceof AddTask) {
                            ((AddTask) addTask).getResponsibleUsers(selectedUsers);
                        }
                    } else if (type == OBSERVERS) {
                        Fragment addTask = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentAddTask.class.getSimpleName());
                        if (addTask instanceof AddTask) {
                            ((AddTask) addTask).getObserverUsers(selectedUsers);
                        }

                    } else if (type == TASK_MEMBERS) {
                        Fragment addCheckList = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentAddChecklist.class.getSimpleName());
                        if (addCheckList instanceof HelperChecklist) {
                            ((HelperChecklist) addCheckList).selectedUsersList(selectedUsers);
                        }
                    }

                    Fragment addProject = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentAddProject.class.getSimpleName());
                    if (addProject instanceof HelperAddProject) {
                        ((HelperAddProject) addProject).getProjectUsers(selectedUsers);
                    }


                });
    }

    private void getProjects() {
        b.rvProject.setVisibility(View.VISIBLE);
        Call<ResponseAllProjects> call = Common.getApi().getAllProjects();
        call.enqueue(new Callback<ResponseAllProjects>() {
            @Override
            public void onResponse(Call<ResponseAllProjects> call, Response<ResponseAllProjects> response) {
                if (response.isSuccessful() || response.body() != null) {
                    adapterProjects.setProjects(response.body().getData());
                    setConnected();
                }
            }

            @Override
            public void onFailure(Call<ResponseAllProjects> call, Throwable t) {
                setNoInternet();
            }
        });

    }

    private void setRecyclerProjectUsers() {
        b.rvSpinnerUsers.setVisibility(View.VISIBLE);
        adapterProjectUsers = new AdapterSpinnerUsers(getContext(), getActivity(), AdapterSpinnerUsers.MULTIPLE);
        b.rvSpinnerUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvSpinnerUsers.setAdapter(adapterProjectUsers);
    }

    private void setRecyclerProject() {
        b.rvProject.setVisibility(View.VISIBLE);
        adapterProjects = new AdapterListProjects(getContext());
        b.rvProject.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.rvProject.setAdapter(adapterProjects);
        adapterProjects.setActivity(getActivity());
    }
}