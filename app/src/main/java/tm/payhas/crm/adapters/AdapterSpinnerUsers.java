package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;

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
import tm.payhas.crm.dataModels.DataProject;
import tm.payhas.crm.dataModels.DataProjectUsers;
import tm.payhas.crm.fragment.FragmentCloudFolder;
import tm.payhas.crm.interfaces.MultiSelector;

public class AdapterSpinnerUsers extends RecyclerView.Adapter<AdapterSpinnerUsers.ViewHolder> {

    private Context context;
    private ArrayList<DataProject> projects = new ArrayList<>();


    public AdapterSpinnerUsers(Context context) {
        this.context = context;
    }


    public void setProjects(ArrayList<DataProject> users) {
        this.projects = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return projects.size();
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
            DataProject oneProjects = projects.get(getAdapterPosition());
            userName.setText(oneProjects.getName());
            main.setOnClickListener(view -> {
                checkBox.setChecked(!checkBox.isChecked());
                if (checkBox.isChecked()) {
                    main.setBackgroundColor(Color.parseColor("#197E69FF"));
                } else {
                    main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

            });

//            DataProjectUsers selectedOne = projects.get(getAdapterPosition());
//            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
//                if (checkBox.isChecked()) {
//                    if (selected.contains(selectedOne)) {
//                        return;
//                    }
//                    selected.add(selectedOne);
//                    main.setBackgroundColor(Color.parseColor("#197E69FF"));
//                    selectedOne.setSelected(true);
//                } else {
//                    selected.remove(selectedOne);
//                    selectedOne.setSelected(false);
//                    main.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                }
//                Fragment cloudFolder = mainFragmentManager.findFragmentByTag(FragmentCloudFolder.class.getSimpleName());
//                if (cloudFolder instanceof MultiSelector) {
//                    ((MultiSelector) cloudFolder).selectedUserList(selected);
//                }
//            });
        }
    }
}
