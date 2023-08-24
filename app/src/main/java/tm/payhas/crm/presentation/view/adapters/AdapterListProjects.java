package tm.payhas.crm.presentation.view.adapters;

import android.app.Activity;
import android.content.Context;
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
import tm.payhas.crm.domain.model.DataProject;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.fragment.FragmentAddTask;
import tm.payhas.crm.domain.interfaces.AddTask;

public class AdapterListProjects extends RecyclerView.Adapter<AdapterListProjects.ViewHolder> {

    private Context context;
    private ArrayList<DataProject> projects = new ArrayList<>();
    private Activity activity;


    public AdapterListProjects(Context context) {
        this.context = context;
    }

    public void setProjects(ArrayList<DataProject> users) {
        this.projects = users;
        notifyDataSetChanged();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkBox.setVisibility(View.GONE);
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
            DataProject oneProjects = projects.get(getAdapterPosition());
            userName.setText(oneProjects.getName());
            main.setOnClickListener(view -> {
                Fragment addTask = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentAddTask.class.getSimpleName());
                if (addTask instanceof AddTask) {
                    ((AddTask) addTask).selectedProjectId(oneProjects);
                }
                getActivity().onBackPressed();

            });
        }
    }
}
