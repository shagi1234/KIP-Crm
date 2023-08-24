package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.domain.helpers.Common.addFragment;
import static tm.payhas.crm.domain.helpers.Common.normalDate;
import static tm.payhas.crm.domain.helpers.StaticMethods.setBackgroundDrawable;
import static tm.payhas.crm.domain.statics.StaticConstants.FINISHED;
import static tm.payhas.crm.domain.statics.StaticConstants.HIGH;
import static tm.payhas.crm.domain.statics.StaticConstants.IN_PROCESS;
import static tm.payhas.crm.domain.statics.StaticConstants.MEDIUM;
import static tm.payhas.crm.domain.statics.StaticConstants.NOT_IMPORTANT;
import static tm.payhas.crm.domain.statics.StaticConstants.NOT_STARTED;
import static tm.payhas.crm.domain.statics.StaticConstants.PRIMARY;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
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
import tm.payhas.crm.domain.model.DataTask;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.fragment.FragmentOneTask;

public class AdapterTasks extends RecyclerView.Adapter<AdapterTasks.ViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<DataTask> tasks = new ArrayList<>();

    public AdapterTasks(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void setTasks(ArrayList<DataTask> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTask;
        private TextView startTime;
        private TextView endTime;
        private TextView status;
        private TextView members;
        private TextView priority;
        private TextView observers;
        private FrameLayout clickableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTask = itemView.findViewById(R.id.task_name);
            startTime = itemView.findViewById(R.id.task_time_start);
            endTime = itemView.findViewById(R.id.task_time_end);
            status = itemView.findViewById(R.id.task_status);
            members = itemView.findViewById(R.id.task_members);
            priority = itemView.findViewById(R.id.task_importancy);
            observers = itemView.findViewById(R.id.observers);
            clickableLayout = itemView.findViewById(R.id.clickable_layout);
        }

        public void bind() {
            DataTask oneTask = tasks.get(getAdapterPosition());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                nameTask.setText(Html.fromHtml(oneTask.getName(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                nameTask.setText(Html.fromHtml(oneTask.getName()));
            }
            startTime.setText(normalDate(oneTask.getStartsAt()));
            endTime.setText(normalDate(oneTask.getFinishesAt()));
            if (oneTask.getStatus() != null)
                setStatus(oneTask.getStatus());
            if (oneTask.getPriority() != null)
                setPriority(oneTask.getPriority());
            observers.setText(String.valueOf(oneTask.getCount().getObserverUsers()));
            members.setText(String.valueOf(oneTask.getCount().getResponsibleUsers()));
            clickableLayout.setOnClickListener(view -> addFragment(ActivityMain.mainFragmentManager, R.id.main_content, FragmentOneTask.newInstance(oneTask.getId())));
        }


        private void setStatus(String statusReceived) {
            switch (statusReceived) {
                case IN_PROCESS:
                    setBackgroundDrawable(context, status, R.color.status_in_process, 0, 50, false, 0);
                    status.setTextColor(activity.getResources().getColor(R.color.status_in_process_text));
                    status.setText(R.string.in_process);
                    break;
                case NOT_STARTED:
                    setBackgroundDrawable(context, status, R.color.status_not_started, 0, 50, false, 0);
                    status.setTextColor(activity.getResources().getColor(R.color.status_not_started_text));
                    status.setText(R.string.not_started);
                    break;
                case FINISHED:
                    setBackgroundDrawable(context, status, R.color.status_finished, 0, 50, false, 0);
                    status.setTextColor(activity.getResources().getColor(R.color.status_finished_text));
                    status.setText(R.string.finished);
                    break;
            }
        }

        private void setPriority(String priorityText) {
            Log.e("Priority", "setPriority: " + priorityText);
            switch (priorityText) {
                case PRIMARY:
                    setBackgroundDrawable(context, priority, R.color.status_in_process, 0, 50, false, 0);
                    priority.setText(R.string.primary);
                    break;
                case MEDIUM:
                    setBackgroundDrawable(context, priority, R.color.status_in_process, 0, 50, false, 0);
                    priority.setText(R.string.medium);
                    break;
                case HIGH:
                    setBackgroundDrawable(context, priority, R.color.status_in_process, 0, 50, false, 0);
                    priority.setText(R.string.high);
                    break;
                case NOT_IMPORTANT:
                    setBackgroundDrawable(context, priority, R.color.status_in_process, 0, 50, false, 0);
                    priority.setText(R.string.neotlozhnyy);
                    break;
            }
        }


    }
}
