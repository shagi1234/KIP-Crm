package tm.payhas.crm.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.dataModels.DataProject;

public class AdapterListProjects extends RecyclerView.Adapter<AdapterListProjects.ViewHolder> {

    private Context context;
    private ArrayList<DataProject> projects = new ArrayList<>();


    public AdapterListProjects(Context context) {
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
        }
    }
}
