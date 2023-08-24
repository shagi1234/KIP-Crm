package tm.payhas.crm.presentation.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.fragment.FragmentAddProject;
import tm.payhas.crm.presentation.view.fragment.FragmentAddTask;
import tm.payhas.crm.domain.interfaces.AddTask;
import tm.payhas.crm.domain.interfaces.HelperAddProject;

public class AdapterSpinnerUsers extends RecyclerView.Adapter<AdapterSpinnerUsers.ViewHolder> implements Filterable {

    private Context context;
    private int type;
    private Activity activity;
    private int executorUserId;
    public static final int SINGULAR = 22;
    public static final int MULTIPLE = 11;
    private String searchText;
    private ArrayList<EntityUserInfo> usersList = new ArrayList<>();
    private ArrayList<EntityUserInfo> selectedUsers = new ArrayList<>();
    private ArrayList<Integer> selectedUserList = new ArrayList<>();
    private ArrayList<EntityUserInfo> filteredUsersList;

    public void setUsersList(ArrayList<EntityUserInfo> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public AdapterSpinnerUsers(Context context, Activity activity, int type) {
        this.context = context;
        this.activity = activity;
        this.type = type;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterPattern = constraint.toString().toLowerCase().trim();
                filteredUsersList = new ArrayList<>();

                for (EntityUserInfo user : usersList) {
                    String userName = user.getPersonalData().getName().toLowerCase();
                    if (userName.contains(filterPattern)) {
                        filteredUsersList.add(user);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredUsersList;
                results.count = filteredUsersList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredUsersList = (ArrayList<EntityUserInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
        getFilter().filter(searchText); // Apply the filter for search text
    }

    public void setSelectedUserList(ArrayList<Integer> selectedUserList) {
        this.selectedUserList = selectedUserList;
        notifyDataSetChanged();
    }

    public ArrayList<EntityUserInfo> getSelectedUsers() {
        return selectedUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntityUserInfo oneUser;
        if (searchText != null && !searchText.isEmpty()) {
            // Use the filteredUsersList when searching
            oneUser = filteredUsersList.get(position);
        } else {
            oneUser = usersList.get(position);
        }

        if (type == SINGULAR) {
            holder.bindSingle(oneUser);
        } else {
            holder.setSelected(oneUser);
            holder.bind(oneUser);
        }

    }

    @Override
    public int getItemCount() {
        if (searchText != null && !searchText.isEmpty()) {
            // Use the filteredUsersList size when searching
            return filteredUsersList.size();
        } else {
            return usersList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        CheckBox checkBox;
        LinearLayout main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.cloud_file);
            checkBox = itemView.findViewById(R.id.checkbox);
            userName = itemView.findViewById(R.id.username);
        }

        public void setChecked(EntityUserInfo oneUser) {
            checkBox.setChecked(true);
            oneUser.setSelected(true);
            main.setBackgroundColor(Color.parseColor("#197E69FF"));
            if (!selectedUsers.contains(oneUser)) {
                selectedUsers.add(oneUser);
                selectedUserList.add(oneUser.getId());
            }
        }

        public void setUnChecked(EntityUserInfo oneUser) {
            checkBox.setChecked(false);
            oneUser.setSelected(false);
            main.setBackgroundColor(Color.parseColor("#FFFFFF"));
            selectedUsers.remove(oneUser);
            selectedUserList.remove((Integer) oneUser.getId());
        }

        public void bind(EntityUserInfo oneUser) {
            setUserInfo(oneUser);
            if (oneUser.isSelected()) {
                setChecked(oneUser);
            } else {
                setUnChecked(oneUser);
            }
            main.setOnClickListener(view -> {
                if (oneUser.isSelected()) {
                    setUnChecked(oneUser);
                } else {
                    setChecked(oneUser);
                }
            });
        }

        private void setUserInfo(EntityUserInfo one) {
            userName.setText(one.getPersonalData().getName());
        }

        public void bindSingle(EntityUserInfo oneUser) {
            main.setBackgroundColor(Color.parseColor("#FFFFFF"));
            userName.setText(oneUser.getPersonalData().getName());
            main.setOnClickListener(view -> {
                executorUserId = oneUser.getId();
                activity.onBackPressed();
                Fragment addProject = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentAddProject.class.getSimpleName());
                Fragment addTask = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentAddTask.class.getSimpleName());
                if (addProject instanceof HelperAddProject) {
                    ((HelperAddProject) addProject).getExecutorUser(oneUser);
                }
                if (addTask instanceof AddTask) {
                    ((AddTask) addTask).setExecutor(oneUser);
                }
            });
        }

        public void setSelected(EntityUserInfo oneUser) {
            boolean isSelected = selectedUserList.contains(oneUser.getId());
            oneUser.setSelected(isSelected);
            if (isSelected) {
                setChecked(oneUser);
            } else {
                setUnChecked(oneUser);
            }
        }
    }
}
