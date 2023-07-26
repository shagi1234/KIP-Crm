package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.Common.normalDate;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.dataModels.DataTask;
import tm.payhas.crm.fragment.FragmentOneTask;

public class AdapterTasks extends RecyclerView.Adapter<AdapterTasks.ViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<DataTask> tasks = new ArrayList<>();

    public AdapterTasks(Context context) {
        this.context = context;
    }

    public void setTasks(ArrayList<DataTask> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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
            status.setText(oneTask.getStatus());
            if (oneTask.getObserverUsers() != null)
                observers.setText(String.valueOf(oneTask.getObserverUsers().size()));
            if (oneTask.getResponsibleUsers() != null)
                members.setText(String.valueOf(oneTask.getResponsibleUsers().size()));
            priority.setText(oneTask.getPriority());
            clickableLayout.setOnClickListener(view -> addFragment(mainFragmentManager, R.id.main_content, FragmentOneTask.newInstance(oneTask.getId())));
        }
    }
}
