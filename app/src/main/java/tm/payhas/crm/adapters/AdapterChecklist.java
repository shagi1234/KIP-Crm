package tm.payhas.crm.adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.api.response.ResponseChecklist;
import tm.payhas.crm.dataModels.DataChecklist;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.preference.AccountPreferences;

public class AdapterChecklist extends RecyclerView.Adapter<AdapterChecklist.ViewHolder> {

    private Context context;
    private ArrayList<DataChecklist> checklists = new ArrayList<>();

    public AdapterChecklist(Context context) {
        this.context = context;
    }

    public void setChecklists(ArrayList<DataChecklist> checklists) {
        this.checklists = checklists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataChecklist oneChecklist = checklists.get(position);
        holder.bind(oneChecklist);
    }

    @Override
    public int getItemCount() {
        return checklists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearProgressIndicator linearIndicator;
        TextView numericIndicator;
        CheckBox checkBox;
        AccountPreferences ac;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.checklist_name);
            linearIndicator = itemView.findViewById(R.id.linear_progress_bar);
            numericIndicator = itemView.findViewById(R.id.checklist_progress_numberic);
            checkBox = itemView.findViewById(R.id.checklist_checkbox);
            ac = new AccountPreferences(context);
        }

        public void bind(DataChecklist oneChecklist) {
            name.setText(oneChecklist.getName());
            if (oneChecklist.isCompleted()) {
                checkBox.setChecked(true);
                linearIndicator.setProgress(100);
                numericIndicator.setText("100%");
            } else {
                linearIndicator.setProgress(0);
                numericIndicator.setText("0%");
            }
            checkBox.setOnClickListener(view -> {
                checkBox.setEnabled(false);
                new Handler().postDelayed(() -> checkBox.setEnabled(true), 200);
            });
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    linearIndicator.setProgress(100);
                    numericIndicator.setText("100%");
                    complete(oneChecklist);
                } else {
                    linearIndicator.setProgress(0);
                    numericIndicator.setText("0%");
                }
            });
        }

        private void complete(DataChecklist oneChecklist) {
            Call<ResponseChecklist> call = Common.getApi().completeChecklist(ac.getToken(), oneChecklist.getId());
            call.enqueue(new Callback<ResponseChecklist>() {
                @Override
                public void onResponse(Call<ResponseChecklist> call, Response<ResponseChecklist> response) {
                    if (response.isSuccessful()) {
                        Log.e("Cheeckbox", "onResponse:");

                    }
                }

                @Override
                public void onFailure(Call<ResponseChecklist> call, Throwable t) {

                }
            });
        }
    }
}
