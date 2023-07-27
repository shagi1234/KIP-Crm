package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.normalDate;
import static tm.payhas.crm.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.statics.StaticConstants.FINISHED;
import static tm.payhas.crm.statics.StaticConstants.IN_PROCESS;
import static tm.payhas.crm.statics.StaticConstants.NOT_STARTED;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
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
    private Activity activity;

    public AdapterProjects(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
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
            Log.e("Projects_status", "bind: "+oneProject.getStatus());
            if (oneProject.getStatus()!=null){
                setStatus(oneProject.getStatus());
            }
            if (oneProject.getProjectParticipants() != null) {
                memberCount.setText(String.valueOf(oneProject.getProjectParticipants().size()));
            } else {
                memberCount.setText("0");

            }
            clicker.setOnClickListener(view -> {
                clicker.setEnabled(false);
                addFragment(mainFragmentManager, R.id.main_content, FragmentOneProject.newInstance(oneProject.getId()));
                new Handler().postDelayed(() -> clicker.setEnabled(true), 200);
            });
        }

        private void setStatus(String statusReceived) {
            switch (statusReceived) {
                case IN_PROCESS:
                    setBackgroundDrawable(context,status, R.color.status_in_process, 0, 50, false, 0);
                    status.setTextColor(activity.getResources().getColor(R.color.status_in_process_text));
                    status.setText(R.string.in_process);
                    break;
                case NOT_STARTED:
                    setBackgroundDrawable(context, status,  R.color.status_not_started, 0, 50, false, 0);
                    status.setTextColor(activity.getResources().getColor(R.color.status_not_started_text));
                    status.setText(R.string.not_started);
                    break;
                case FINISHED:
                    setBackgroundDrawable(context, status,  R.color.status_finished, 0, 50, false, 0);
                    status.setTextColor(activity.getResources().getColor(R.color.status_finished_text));
                    status.setText(R.string.finished);
                    break;
            }
        }

    }
}
