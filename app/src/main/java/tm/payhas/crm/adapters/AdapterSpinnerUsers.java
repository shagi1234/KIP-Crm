package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.api.data.dto.DtoUserInfo;
import tm.payhas.crm.fragment.FragmentAddProject;
import tm.payhas.crm.interfaces.HelperAddProject;

public class AdapterSpinnerUsers extends RecyclerView.Adapter<AdapterSpinnerUsers.ViewHolder> {

    private Context context;
    private int type;
    private Activity activity;
    private int executorUserId;
    public static final int SINGULAR = 22;
    public static final int MULTIPLE = 11;
    private ArrayList<DtoUserInfo> usersList = new ArrayList<>();
    private ArrayList<DtoUserInfo> selectedUsers = new ArrayList<>();

    public void setUsersList(ArrayList<DtoUserInfo> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public AdapterSpinnerUsers(Context context, Activity activity, int type) {
        this.context = context;
        this.activity = activity;
        this.type = type;
    }

    public ArrayList<DtoUserInfo> getSelectedUsers() {
        return selectedUsers;
    }

    public int getExecutorUserId() {
        return executorUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (type == SINGULAR) {
            DtoUserInfo oneUSer = usersList.get(position);
            holder.bindSingle(oneUSer);
        } else {
            holder.bind();

        }

    }

    @Override
    public int getItemCount() {
        return usersList.size();
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

        public void bind() {
            main.setBackgroundColor(Color.parseColor("#FFFFFF"));
            DtoUserInfo oneUser = usersList.get(getAdapterPosition());
            userName.setText(oneUser.getPersonalData().getName());
            main.setOnClickListener(view -> {
                checkBox.setChecked(!checkBox.isChecked());
                if (checkBox.isChecked()) {
                    main.setBackgroundColor(Color.parseColor("#197E69FF"));
                } else {
                    main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

            });

            DtoUserInfo selectedOne = usersList.get(getAdapterPosition());
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (checkBox.isChecked()) {
                    if (selectedUsers.contains(selectedOne)) {
                        return;
                    }
                    selectedUsers.add(selectedOne);
                    main.setBackgroundColor(Color.parseColor("#197E69FF"));
                    selectedOne.setSelected(true);
                } else {
                    selectedUsers.remove(selectedOne);
                    selectedOne.setSelected(false);
                    main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

            });
            if (selectedOne.isSelected()) {
                checkBox.setChecked(true);
                main.setBackgroundColor(Color.parseColor("#197E69FF"));
            } else {
                checkBox.setChecked(false);
                main.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }
        }

        public void bindSingle(DtoUserInfo oneUSer) {
            main.setBackgroundColor(Color.parseColor("#FFFFFF"));
            userName.setText(oneUSer.getPersonalData().getName());
            main.setOnClickListener(view -> {
                int exUs = oneUSer.getId();
                exUs = executorUserId;
                activity.onBackPressed();
                Fragment addProject = mainFragmentManager.findFragmentByTag(FragmentAddProject.class.getSimpleName());
                if (addProject instanceof HelperAddProject) {
                    ((HelperAddProject) addProject).getExecutorUser(oneUSer);
                }
            });
        }
    }
}
