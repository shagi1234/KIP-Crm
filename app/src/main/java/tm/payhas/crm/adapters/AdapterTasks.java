package tm.payhas.crm.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.dataModels.DataTask;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTask = itemView.findViewById(R.id.task_name);
            startTime = itemView.findViewById(R.id.task_time_start);
            endTime = itemView.findViewById(R.id.task_time_end);
            status = itemView.findViewById(R.id.task_status);
        }

        public void bind() {
            DataTask oneTask = tasks.get(getAdapterPosition());
            nameTask.setText(oneTask.getDescription());
            startTime.setText(oneTask.getStartsAt());
            endTime.setText(oneTask.getFinishesAt());
            status.setText(oneTask.getStatus());
        }
    }
}
