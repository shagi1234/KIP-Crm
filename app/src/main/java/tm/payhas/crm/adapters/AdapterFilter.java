package tm.payhas.crm.adapters;

import static tm.payhas.crm.helpers.StaticMethods.checkLang;

import android.content.Context;
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
import tm.payhas.crm.dataModels.DataFilter;

public class AdapterFilter extends RecyclerView.Adapter<AdapterFilter.ViewHolder> {

    private Context context;
    private ArrayList<DataFilter> varianceItems = new ArrayList<>();
    private ArrayList<DataFilter> selectedArray = new ArrayList<>();
    private TextView selectDeselectText;

    public AdapterFilter(Context context, TextView selectDeselectText) {
        this.context = context;
        this.selectDeselectText = selectDeselectText;
    }

    public ArrayList<DataFilter> getSelectedArray() {
        return selectedArray;
    }

    public void setVarianceItems(ArrayList<DataFilter> varianceItems) {
        this.varianceItems = varianceItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataFilter varianceItem = varianceItems.get(position);
        holder.bind(varianceItem);
        holder.setSelected(varianceItem);

    }

    public void addSelected(DataFilter varianceItem) {
        selectedArray.add(varianceItem);
    }

    @Override
    public int getItemCount() {
        return varianceItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView varianceText;
        private LinearLayout layCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            varianceText = itemView.findViewById(R.id.variance_text);
            layCheckbox = itemView.findViewById(R.id.lay_checkbox);
        }

        public void bind(DataFilter varianceItem) {
            varianceText.setText(checkLang(context, varianceItem.getStrTm(), varianceItem.getStrRu()));
            layCheckbox.setOnClickListener(view -> {
                varianceItem.setSelected(!varianceItem.isSelected()); // Toggle the selection state of the item
                notifyDataSetChanged(); // Update the RecyclerView


                if (varianceItem.isSelected()) {
                    addSelected(varianceItem); // Add the selected item to the selectedArray
                } else {
                    selectedArray.remove(varianceItem); // Remove the item from the selectedArray if it's unselected
                }
                // Check if all items are selected or not
                boolean allItemsSelected = true;
                for (DataFilter item : varianceItems) {
                    if (!item.isSelected()) {
                        allItemsSelected = false;
                        break;
                    }
                }


                // Change the text of the "Select All" button based on the selection state of all items
                if (allItemsSelected) {
                    selectDeselectText.setText("Deselect All");
                } else {
                    selectDeselectText.setText("Select All");
                }
            });
        }

        public void setSelected(DataFilter varianceItem) {
            checkBox.setChecked(varianceItem.isSelected());
        }
    }
}
