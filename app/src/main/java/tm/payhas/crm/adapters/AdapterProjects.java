package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.normalDate;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.dataModels.DataProject;
import tm.payhas.crm.fragment.FragmentOneProject;

public class AdapterProjects extends RecyclerView.Adapter<AdapterProjects.ViewHolder> {

    private Context context;
    private ArrayList<DataProject> projects = new ArrayList<>();

    public AdapterProjects(Context context) {
        this.context = context;
    }

    public void setProjects(ArrayList<DataProject> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_projects, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataProject oneProject = projects.get(position);
        holder.bind(oneProject);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView projectName;
        private TextView startTime;
        private TextView endTime;
        private TextView status;
        private TextView memberCount;
        private TextView count;
        private FrameLayout clicker;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.project_name);
            startTime = itemView.findViewById(R.id.project_time_start);
            endTime = itemView.findViewById(R.id.project_end_time);
            status = itemView.findViewById(R.id.project_status);
            memberCount = itemView.findViewById(R.id.project_user_count);
            clicker = itemView.findViewById(R.id.clickable_layout);
        }

        public void bind(DataProject oneProject) {
            projectName.setText(oneProject.getName());
            startTime.setText(normalDate(oneProject.getStartsAt()));
            endTime.setText(normalDate(oneProject.getDeadline()));
            memberCount.setText(String.valueOf(oneProject.getProjectParticipants().size()));
            clicker.setOnClickListener(view -> {
                clicker.setEnabled(false);
                addFragment(mainFragmentManager, R.id.main_content, FragmentOneProject.newInstance(oneProject.getId()));
                new Handler().postDelayed(() -> clicker.setEnabled(true),200);
            });
        }
    }
}
