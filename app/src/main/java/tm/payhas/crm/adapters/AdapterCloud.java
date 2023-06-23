package tm.payhas.crm.adapters;

import static tm.payhas.crm.activity.ActivityMain.mainFragmentManager;
import static tm.payhas.crm.api.network.Network.BASE_URL;
import static tm.payhas.crm.helpers.Common.addFragment;
import static tm.payhas.crm.helpers.FileDownloader.downloadFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.dataModels.DataFolder;
import tm.payhas.crm.fragment.FragmentCloudFile;
import tm.payhas.crm.fragment.FragmentCloudFolder;
import tm.payhas.crm.interfaces.DataFileSelectedListener;

public class AdapterCloud extends RecyclerView.Adapter<AdapterCloud.ViewHolder> {

    private Context context;
    private Activity activity;
    public static int CLOUD_TYPE_FOLDER = 1;
    public static int CLOUD_TYPE_FILE = 2;
    private int type;
    private boolean isSelectable = false;
    private ArrayList<DataFolder> selected = new ArrayList<>();
    private ArrayList<DataFolder> all = new ArrayList<>();

    public AdapterCloud(Context context, Activity activity, int type) {
        this.context = context;
        this.activity = activity;
        this.type = type;

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAll(ArrayList<DataFolder> all) {
        this.all = all;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cloud_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isSelectable) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
        DataFolder oneFolder = all.get(position);
        holder.bind(oneFolder);

    }

    @Override
    public int getItemCount() {
        return all.size();
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
        selected.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout main;
        TextView fileName;
        CheckBox checkBox;
        ImageView folder;
        ImageView downloader;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.cloud_file);
            checkBox = itemView.findViewById(R.id.checkbox);
            fileName = itemView.findViewById(R.id.file_name);
            folder = itemView.findViewById(R.id.folder);
            downloader = itemView.findViewById(R.id.file_downloader);

        }

        @SuppressLint("ResourceAsColor")
        public void bind(DataFolder oneFolder) {

            if (type == CLOUD_TYPE_FOLDER) {
                int folderColor = Color.parseColor(oneFolder.getColor());
                folder.setColorFilter(folderColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            } else if (type == CLOUD_TYPE_FILE) {
                setFileInfo(oneFolder);
                downloader.setVisibility(View.VISIBLE);
            }

            if (type == CLOUD_TYPE_FILE) {
                downloader.setOnClickListener(view -> {
                    downloader.setEnabled(false);
                    downloadFile(context, BASE_URL + oneFolder.getFileUrl(), oneFolder.getFileName());
                    Log.e("DownloadClicked", "bind: ");
                });
            }


            main.setBackgroundColor(Color.parseColor("#FFFFFF"));
            fileName.setText(oneFolder.getFileName());

            if (isSelectable) {
                main.setOnClickListener(view -> {
                    checkBox.setChecked(!checkBox.isChecked());
                    if (checkBox.isChecked()) {
                        main.setBackgroundColor(Color.parseColor("#197E69FF"));
                    } else {
                        main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }

                });
            } else {
                if (type == CLOUD_TYPE_FOLDER)
                    main.setOnClickListener(view -> {
                        main.setEnabled(false);
                        addFragment(mainFragmentManager, R.id.main_content, FragmentCloudFile.newInstance(oneFolder.getFileUrl()));
                        new Handler().postDelayed(() -> main.setEnabled(true), 200);
                    });
            }
            DataFolder selectedOne = all.get(getAdapterPosition());
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (checkBox.isChecked()) {
                    if (selected.contains(selectedOne)) {
                        return;
                    }
                    selected.add(selectedOne);
                    main.setBackgroundColor(Color.parseColor("#197E69FF"));
                    oneFolder.setSelected(true);
                } else {
                    selected.remove(selectedOne);
                    oneFolder.setSelected(false);
                    main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                Fragment cloudFolder = mainFragmentManager.findFragmentByTag(FragmentCloudFolder.class.getSimpleName());
                Fragment cloudFile = mainFragmentManager.findFragmentByTag(FragmentCloudFile.class.getSimpleName());
                if (cloudFile instanceof DataFileSelectedListener) {
                    ((DataFileSelectedListener) cloudFile).multiSelectedArray(selected);
                }
                if (cloudFolder instanceof DataFileSelectedListener) {
                    ((DataFileSelectedListener) cloudFolder).multiSelectedArray(selected);
                }
            });
            if (selectedOne.isSelected()) {
                checkBox.setChecked(true);
                main.setBackgroundColor(Color.parseColor("#197E69FF"));
            } else {
                checkBox.setChecked(false);
                main.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }

        }

        private void setFileInfo(DataFolder oneFolder) {
            folder.setImageResource(R.drawable.ic_documenyt_file);
            fileName.setText(oneFolder.getFileName());

        }
    }
}
